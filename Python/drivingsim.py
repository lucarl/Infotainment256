"""
An example of a simulation.

This example implement a simple event simulation that model the a revving
engine and emit data events on gear and vehicle speed.

For information on how to use SimPy, go to https://simpy.readthedocs.io/en/latest/index.html.
"""

# we need the carapi to push events to AndroidCAR
import carapi
# we need the property database for the event definitions
import propdb
# SimPy is used for discrete event simulation and we use the realtime variant
import simpy.rt

import random
import time

# a type for simulating the state of the vehicle
class vehicle_dynamics: pass

vehicle_data = vehicle_dynamics()

vehicle_data.speed = 0.0
vehicle_data.gear = 1
vehicle_data.rpm = 0.0
vehicle_data.fuelDistance = 500.0;

def revProcess(env):
    """
    This process simulate the revving of the engine.
    """
    while True:
    
        randExtra = random.randint(-1,1)
        
        if vehicle_data.rpm < 3000:
            vehicle_data.rpm += 50
            vehicle_data.fuelDistance -= 1 + randExtra
        elif vehicle_data.gear < 5:
            vehicle_data.rpm = vehicle_data.speed * 50.0 / (vehicle_data.gear + 1)
            vehicle_data.gear += 1
            
        vehicle_data.speed = vehicle_data.rpm * vehicle_data.gear / 100.0

        yield env.timeout(0.025)
        
def randomMotorwayDriving(env):
    """
    This process simulate the revving of the engine.
    """
    while True:
    
        RNGgain = random.randint(0,10)
        rpmVariation = random.randint(1,100)
        randomBraking = random.randint(1,1000)
        brakeVariance = random.randint(-1,2)
        
        if randomBraking >= 0 and randomBraking <= 5:
            vehicle_data.rpm = 2000+brakeVariance*250
            vehicle_data.gear = 4
            while vehicle_data.speed > 80 + brakeVariance*10:
                vehicle_data.speed -= 1
                
                 
        if vehicle_data.rpm < 3000:
            if RNGgain >= 0 and RNGgain <= 3:
                vehicle_data.rpm += rpmVariation
            else:
                vehicle_data.rpm += rpmVariation
        elif vehicle_data.gear < 5:
            vehicle_data.rpm = vehicle_data.speed * 50.0 / (vehicle_data.gear + 1)
            vehicle_data.gear += 1
        else:
            if RNGgain >= 0 and RNGgain <= 5:
                vehicle_data.rpm -= rpmVariation
            
        vehicle_data.speed = vehicle_data.rpm * vehicle_data.gear / 100.0

        yield env.timeout(0.05)
        
def revProcessTraffic(env):
    """
    This process simulate the driving during heavy traffic
    """
    while True:
        
        delay = random.randint(1,30)
            
        if delay==1:
            delay = random.randint(1,5)
            vehicle_data.rpm = 1000;
            vehicle_data.gear = 1;  
            vehicle_data.speed = 0;        
            time.sleep(delay)
            
        if vehicle_data.rpm < 2000:
            vehicle_data.rpm += 50
        elif vehicle_data.gear < 2:
            vehicle_data.rpm = vehicle_data.speed * 100.0 / (vehicle_data.gear + 1)
            vehicle_data.gear += 1
            
        vehicle_data.speed = vehicle_data.rpm * vehicle_data.gear / 100.0

        yield env.timeout(0.025)

def gearReporter(env, api):
    """
    This process send the current gear to the AndroidCAR instance.
    """
    while True:
        
        api.injectInteger(propdb.PROP_GEAR_SELECTION, vehicle_data.gear)
        yield env.timeout(1.0)
        
def RPMReporter(env, api):
    """
    This process send the current RPM to the AndroidCAR instance.
    """
    while True:
        
        api.injectFloat(propdb.PROP_ENGINE_RPM, vehicle_data.rpm)
        yield env.timeout(0.2)
        
def fueldistanceReporter(env, api):
    """
    This process send the current RPM to the AndroidCAR instance.
    """
    while True:
        
        api.injectFloat(propdb.PROP_FUEL_IN_DISTANCE, vehicle_data.fuelDistance)
        yield env.timeout(0.2)
        
def speedReporter(env, api):
    """
    This process send the current speed to the AndroidCAR instance.
    """
    while True:
        
        api.injectFloat(propdb.PROP_PERF_VEHICLE_SPEED, vehicle_data.speed)
        yield env.timeout(0.1)

# run
if __name__ == '__main__':

    # create an interface to the AndroidCAR running on an emulator
    api = carapi.AndroidCARInterface()
    # create an event simulation environment
    env = simpy.rt.RealtimeEnvironment(factor=1, strict=False)

    # create the processes
    #revp = env.process(revProcess(env))
    mwdrivingp = env.process(randomMotorwayDriving(env))
    rpmp = env.process(RPMReporter(env, api))
    fuelp = env.process(fueldistanceReporter(env, api))
    gearp = env.process(gearReporter(env, api))

    # start simulation and run it for 60 seconds
    env.run(until=60.0)

