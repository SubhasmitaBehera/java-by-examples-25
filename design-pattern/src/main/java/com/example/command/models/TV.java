package com.example.command.models;

import com.example.command.contracts.Device;

public class TV implements Device {
    @Override
    public void turnOn() {
        System.out.println("TV is now on");
    }

    @Override
    public void turnOff() {
        System.out.println("TV is now off");
    }

}
