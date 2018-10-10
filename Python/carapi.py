"""
This module provide an interface to an Android emulator running an image with a
CAR API. The functionality in the module use use the Android Debug Bridge to
inject events. Thus, it is expected that the adb command is on the path and
that the emulator is reachable.

The method used is to call the following command:
'adb shell dumpsys activity service com.android.car inject-event <propid> <args...>'
It is not fully understood how this command works. 'adb shell' invoke the
Android Shell in the emulator/device. Then the current hypothesis is that
'dumpsys activity service com.android.car' will point to a runtime object and
'inject-event <propid> <args...>' will evaluate that object's 'inject-event'
method with the given arguments.

It is not a very robust functionality, as is, and it is not thread safe. A
single instance of the interface is advisable for each simulation run and the
current design allow for only one emulator running on the host.
"""

import subprocess, sys, propdb


class AndroidCARInterface:
    """
    This class embody the interface functionality to the emulator. It works by
    transforming property identifiers and values into a matching call to the
    Android Debug Bridge. The call utilize a injection mechanism (seemingly) put
    in place by Google developers as a means to test their builds. The mechanism
    has further been expanded by Semcon to accept more types.
    """

    _props = {}

    def __init__(self):
        """
        Initialize class data. Observe that this method make use of
        some Python metamodel introspection to
        :return:modify the property database.
        """
        if len(AndroidCARInterface._props) == 0:

            AndroidCARInterface._props = propdb.processDatabase()

    def listAvailableProps(self, all=False):
        """
        List all properties, their type and internal identifier that are usable
        through this interface.
        """
        for pinfo in [pinfo
                     for _, pinfo in sorted(AndroidCARInterface._props.items())
                     if all or pinfo['prop_type'] != 'TYPE_NOT_SUPPORTED']:
            
            print("%-40s %-8s 0x%08x" % (pinfo['prop_name'],
                                         pinfo['prop_type'],
                                         pinfo['propid']))

    def inject(self, prop, fval=None, ival=None, zone=None, bval=None):
        """
        Inject a property value into the target emulator.
        """

        # the format of the adb call is: command prefix...
        cmd_str = 'adb shell dumpsys activity service com.android.car inject-event'

        type_code = prop['prop_type']

        # ...followed by type specific call format...
        if type_code == 'INT':
            cmd_str += ' global-integer %d %d' % (prop['propid'], ival)
        elif type_code == 'BOOL':
            cmd_str += ' zoned-boolean %d %d %s' % (prop['propid'],
                                                    zone,
                                                    'true' if bval else 'false')
        elif type_code == 'FLOAT':
            cmd_str += ' global-float %d %f' % (prop['propid'], fval)
        else:
            raise TypeError("event's type is not supported")

        # ...and end by throwing away subprocess standard output
        cmd_str += ' > /dev/null' 

        # invoke command, raises CalledProcessError if called command fails
        subprocess.check_call(cmd_str.split())

    def injectInteger(self, prop, ival):
        """
        Convenience method for sending integers.
        """
        self.inject(prop, ival=ival)

    def injectZonedBoolean(self, prop, zone, bval):
        """
        Convenience method for sending booleans.
        """

        self.inject(prop, zone=zone, bval=bval)

    def injectFloat(self, prop, fval):
        """
        Convenience method for sending float values.
        """

        self.inject(prop, fval=fval)
