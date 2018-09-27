"""
Translator process that subscribe to MQTT messages emitted from a SecureGateway
instance and map them to a AndroidCAR property event that is then injected
using carapi.

To use it modify the sgt.ini file to point to the SecureGateway and start the
process. Use -h (or --help) for command line options to set IP address and
port to SecureGateway.

The module is written for Python 3.5+ and assume that the Paho MQTT client for
Python framework is present on the host (see http://www.eclipse.org/paho/).
"""

import propdb, carapi, argparse, functools, configparser
import paho.mqtt.client as mqtt


class SecureGatewayTranslator(mqtt.Client):
    """
    Translator process type. This class implement a MQTT client that listens
    to a SecureGateway and its topics as defined in the configuration. For each
    Android event that has a mapped topic a forwarding is made to an Android
    emulator present on the host.
    """
    def __init__(self):

        mqtt.Client.__init__(self)

        # read configuration
        config = configparser.ConfigParser()
        config.read('sgt.ini')

        # get and parse any command line parameters
        arg_parser = argparse.ArgumentParser()
        arg_parser.add_argument('-s', '--server',
                                default=config['CONNECT']['server_addr'],
                                type=str,
                                help="IP address to MQTT broker, "
                                     "overrides config file")
        arg_parser.add_argument('-p', '--port',
                                default=config['CONNECT']['server_port'],
                                type=int,
                                help="port used by MQTT broker, "
                                     "overrides config file")
        args = arg_parser.parse_args()

        # connect to SecureGateway (or any other MQTT server)
        self.server_addr = args.server
        self.server_port = args.port

        self.connect(self.server_addr, self.server_port)

        # setup property/event database
        self.props = propdb.processDatabase()
        self.translatable = dict([(prop["sgid"], prop)
                                  for _, prop in self.props.items()
                                  if "sgid" in prop])

        # setup an interface to AndroidCAR on emulator
        self.car = carapi.AndroidCARInterface()

        # subscribe to all translatable events in database
        for sgid in self.translatable:
            self.subscribe(sgid)
            self.message_callback_add(sgid,
                                      functools.partial(self.handler, sgid))

    def on_connect(self, *args):
        """
        Called when connected to MQTT broker/SecureGateway.
        """
        print("Connected...", args)

    def on_disconnect(self, *args):
        """
        Called when connected to MQTT broker/SecureGateway.
        """
        print("Disconnected...", args)

    def on_message(self, *args):
        """
        Note that this method is only called for topics not having a
        specific handler assigned (normally none).
        """
        print("Message received: ", args)

    def handler(self, sgid, client, userdata, message):
        """
        Handler callback for specific topic. A partially instantiated instance
        of this handler is supplied for each topic subscribed to. It will
        extract the value, consult the mapping and pass the value on to
        the Android emulator running AndroidCAR.
        """

        # get the translation
        edata = self.translatable[sgid]
        etype = edata['prop_type']

        # inject event into emulator
        if etype == 'INT':
            self.car.injectInteger(edata, int(message.payload))
        elif etype == 'FLOAT':
            self.car.injectFloat(edata, float(message.payload))
        elif etype == 'BOOL':
            self.car.injectZonedBoolean(edata, 0, bool(message.payload))
        else:
            # fail silently
            pass

    def run(self):
        """
        Start the translator process. Does not (normally) return.
        """
        print("Start loop...")
        self.loop_forever()


if __name__ == '__main__':

    # create and run translator
    sgt = SecureGatewayTranslator()
    sgt.run()

    print("ERROR: unexpected exit")