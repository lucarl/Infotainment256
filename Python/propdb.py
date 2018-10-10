"""
This file is defining the event/property database used by the SecureGateway (SG)
translator and for using the event simulator interface (carapi.py).
The database is built as a set of data structures (dictionaries), one for each
event defined by the AndroidCAR environment. For each event the identifier used
in AndroidCAR is listed and also the SG MQTT topic for the corresponding vehicle
signal.
A module using the database call processDatabase() and is then given a
dictionary holding all event/properties with the event variables as a strings
for keys. Also, each event is augumented with its value type and identifier in
string format.

To add a property to the database at least the AndroidCAR identifier have to
be supplied and the name of the property must start with the string PROP_.
Example:data

PROP_MY_OWN_EVENT = {"propid": 0x234567890}

For more information on the Android defined events, the type that define them
and the way there identifiers are constructed, see https://source.android.com/reference/hidl/android/hardware/automotive/vehicle/2.0/types.

"""

import sys


# base listing of Google defined properties
PROP_INVALID = {"propid": 0x0}
PROP_INFO_VIN = {"propid": 0x11100100}
PROP_INFO_MAKE = {"propid": 0x11100101}
PROP_INFO_MODEL = {"propid": 0x11100102}
PROP_INFO_MODEL_YEAR = {"propid": 0x11400103}
PROP_INFO_FUEL_CAPACITY = {"propid": 0x11600104, "sgid": "test/fuelcapacity"}
PROP_PERF_ODOMETER = {"propid": 0x11600204, "sgid": "test/odometer"}
PROP_PERF_VEHICLE_SPEED = {"propid": 0x11600207, "sgid": "test/vehiclespeed"}
PROP_ENGINE_COOLANT_TEMP = {"propid": 0x11600301, "sgid": "test/coolanttemp"}
PROP_ENGINE_OIL_TEMP = {"propid": 0x11600304, "sgid": "test/oiltemp"}
PROP_ENGINE_RPM = {"propid": 0x11600305, "sgid": "test/rpm"}
PROP_GEAR_SELECTION = {"propid": 0x11400400, "sgid": "test/gearselection"}
PROP_CURRENT_GEAR = {"propid": 0x11400401, "sgid": "test/currentgear"}
PROP_PARKING_BRAKE_ON = {"propid": 0x11200402}
PROP_DRIVING_STATUS = {"propid": 0x11400404}
PROP_FUEL_LEVEL_LOW = {"propid": 0x11200405}
PROP_FUEL_IN_DISTANCE = {"propid": 0x21600406} #my own property for fuel     <----------------------------
PROP_NIGHT_MODE = {"propid": 0x11200407}
PROP_TURN_SIGNAL_STATE = {"propid": 0x11400408}
PROP_IGNITION_STATE = {"propid": 0x11400409, "sgid": "test/ignitionstate"}
PROP_HVAC_FAN_SPEED = {"propid": 0x12400500}
PROP_HVAC_FAN_DIRECTION = {"propid": 0x12400501}
PROP_HVAC_TEMPERATURE_CURRENT = {"propid": 0x12600502}
PROP_HVAC_TEMPERATURE_SET = {"propid": 0x12600503}
PROP_HVAC_DEFROSTER = {"propid": 0x13200504}
PROP_HVAC_AC_ON = {"propid": 0x12200505}
PROP_HVAC_MAX_AC_ON = {"propid": 0x12200506}
PROP_HVAC_MAX_DEFROST_ON = {"propid": 0x12200507}
PROP_HVAC_RECIRC_ON = {"propid": 0x12200508}
PROP_HVAC_DUAL_ON = {"propid": 0x12200509}
PROP_HVAC_AUTO_ON = {"propid": 0x1220050a}
PROP_HVAC_SEAT_TEMPERATURE = {"propid": 0x1540050b}
PROP_HVAC_SIDE_MIRROR_HEAT = {"propid": 0x1440050c}
PROP_HVAC_STEERING_WHEEL_TEMP = {"propid": 0x1140050d}
PROP_HVAC_TEMPERATURE_UNITS = {"propid": 0x1240050e}
PROP_HVAC_ACTUAL_FAN_SPEED_RPM = {"propid": 0x1240050f}
PROP_HVAC_FAN_DIRECTION_AVAILABLE = {"propid": 0x12400511}
PROP_HVAC_POWER_ON = {"propid": 0x12200510}
PROP_ENV_OUTSIDE_TEMPERATURE = {"propid": 0x11600703}
PROP_ENV_CABIN_TEMPERATURE = {"propid": 0x11600704}
PROP_RADIO_PRESET = {"propid": 0x11410801}
PROP_AUDIO_FOCUS = {"propid": 0x11410900}
PROP_AUDIO_FOCUS_EXT_SYNC = {"propid": 0x11410910}
PROP_AUDIO_VOLUME = {"propid": 0x11410901}
PROP_AUDIO_VOLUME_EXT_SYNC = {"propid": 0x11410911}
PROP_AUDIO_VOLUME_LIMIT = {"propid": 0x11410902}
PROP_AUDIO_ROUTING_POLICY = {"propid": 0x11410903}
PROP_AUDIO_HW_VARIANT = {"propid": 0x11400904}
PROP_AUDIO_EXT_ROUTING_HINT = {"propid": 0x11410905}
PROP_AUDIO_STREAM_STATE = {"propid": 0x11410906}
PROP_AUDIO_PARAMETERS = {"propid": 0x11100907}
PROP_AP_POWER_STATE = {"propid": 0x11410a00}
PROP_DISPLAY_BRIGHTNESS = {"propid": 0x11400a01}
PROP_AP_POWER_BOOTUP_REASON = {"propid": 0x11400a02}
PROP_HW_KEY_INPUT = {"propid": 0x11410a10}
PROP_INSTRUMENT_CLUSTER_INFO = {"propid": 0x11410a20}
PROP_UNIX_TIME = {"propid": 0x11500a30}
PROP_CURRENT_TIME_IN_SECONDS = {"propid": 0x11400a31}
PROP_DOOR_POS = {"propid": 0x16400b00}
PROP_DOOR_MOVE = {"propid": 0x16400b01}
PROP_DOOR_LOCK = {"propid": 0x16200b02}
PROP_MIRROR_Z_POS = {"propid": 0x14400b40}
PROP_MIRROR_Z_MOVE = {"propid": 0x14400b41}
PROP_MIRROR_Y_POS = {"propid": 0x14400b42}
PROP_MIRROR_Y_MOVE = {"propid": 0x14400b43}
PROP_MIRROR_LOCK = {"propid": 0x11200b44}
PROP_MIRROR_FOLD = {"propid": 0x11200b45}
PROP_SEAT_MEMORY_SELECT = {"propid": 0x15400b80}
PROP_SEAT_MEMORY_SET = {"propid": 0x15400b81}
PROP_SEAT_BELT_BUCKLED = {"propid": 0x15200b82}
PROP_SEAT_BELT_HEIGHT_POS = {"propid": 0x15400b83}
PROP_SEAT_BELT_HEIGHT_MOVE = {"propid": 0x15400b84}
PROP_SEAT_FORE_AFT_POS = {"propid": 0x15400b85}
PROP_SEAT_FORE_AFT_MOVE = {"propid": 0x15400b86}
PROP_SEAT_BACKREST_ANGLE_1_POS = {"propid": 0x15400b87}
PROP_SEAT_BACKREST_ANGLE_1_MOVE = {"propid": 0x15400b88}
PROP_SEAT_BACKREST_ANGLE_2_POS = {"propid": 0x15400b89}
PROP_SEAT_BACKREST_ANGLE_2_MOVE = {"propid": 0x15400b8a}
PROP_SEAT_HEIGHT_POS = {"propid": 0x15400b8b}
PROP_SEAT_HEIGHT_MOVE = {"propid": 0x15400b8c}
PROP_SEAT_DEPTH_POS = {"propid": 0x15400b8d}
PROP_SEAT_DEPTH_MOVE = {"propid": 0x15400b8e}
PROP_SEAT_TILT_POS = {"propid": 0x15400b8f}
PROP_SEAT_TILT_MOVE = {"propid": 0x15400b90}
PROP_SEAT_LUMBAR_FORE_AFT_POS = {"propid": 0x15400b91}
PROP_SEAT_LUMBAR_FORE_AFT_MOVE = {"propid": 0x15400b92}
PROP_SEAT_LUMBAR_SIDE_SUPPORT_POS = {"propid": 0x15400b93}
PROP_SEAT_LUMBAR_SIDE_SUPPORT_MOVE = {"propid": 0x15400b94}
PROP_SEAT_HEADREST_HEIGHT_POS = {"propid": 0x11400b95}
PROP_SEAT_HEADREST_HEIGHT_MOVE = {"propid": 0x15400b96}
PROP_SEAT_HEADREST_ANGLE_POS = {"propid": 0x15400b97}
PROP_SEAT_HEADREST_ANGLE_MOVE = {"propid": 0x15400b98}
PROP_SEAT_HEADREST_FORE_AFT_POS = {"propid": 0x15400b99}
PROP_SEAT_HEADREST_FORE_AFT_MOVE = {"propid": 0x15400b9a}
PROP_WINDOW_POS = {"propid": 0x11400bc0}
PROP_WINDOW_MOVE = {"propid": 0x11400bc1}
PROP_WINDOW_VENT_POS = {"propid": 0x11400bc2}
PROP_WINDOW_VENT_MOVE = {"propid": 0x11400bc3}
PROP_WINDOW_LOCK = {"propid": 0x11200bc4}
PROP_WHEEL_TICK = {"propid": 0x11e00306}
PROP_ABS_ACTIVE = {"propid": 0x1120040a}
PROP_TRACTION_CONTROL_ACTIVE = {"propid": 0x1120040b}
PROP_HVAC_AUTO_RECIRC_ON = {"propid": 0x12200512}
PROP_VEHICLE_MAP_SERVICE = {"propid": 0x11e00c00}
PROP_OBD2_LIVE_FRAME = {"propid": 0x11e00d00}
PROP_OBD2_FREEZE_FRAME = {"propid": 0x11e00d01}
PROP_OBD2_FREEZE_FRAME_INFO = {"propid": 0x11e00d02}
PROP_OBD2_FREEZE_FRAME_CLEAR = {"propid": 0x11e00d03}


# processed database for use in other modules
_props = {}

def processDatabase():
    """
    Process the database into a format more usable for modules sgtranslator and
    carapi. Observe that this function make use of Python's introspection into
    its metamodel to build the resulting property dictionary.
    :return: A dicitionary with all defined events/properties used.
    """
    if len(_props) == 0:
        module = sys.modules[__name__]
        for prop_name in [prop_name
                          for prop_name in dir(module)
                          if prop_name.startswith('PROP_')]:
            module.__dict__[prop_name]['prop_name'] = prop_name
            module.__dict__[prop_name]['prop_type'] = _prop_type(module.__dict__[prop_name])
            _props[prop_name] = module.__dict__[prop_name]

    return _props


def _prop_type(prop):
    """
    Infer the type of a property from its internal identifier value.
    The property identifier code have the type coded into it. This method extract that information and return a type constant.
    """
    # property type idientifiers
    _STRING = 0x100000
    _BOOLEAN = 0x200000
    _INT32 = 0x400000
    _INT32_VEC = 0x410000
    _INT64 = 0x500000
    _FLOAT = 0x600000
    _FLOAT_VEC = 0x610000
    _BYTES = 0x700000
    _COMPLEX = 0xe00000
    _TYPE_MASK = 0xff0000

    type_code = _TYPE_MASK & prop['propid']
    type_str = 'TYPE_NOT_SUPPORTED'

    if type_code == _INT32:
        type_str = 'INT'
    elif type_code == _BOOLEAN:
        type_str = 'BOOL'
    elif type_code == _FLOAT:
        type_str = 'FLOAT'

    return type_str
