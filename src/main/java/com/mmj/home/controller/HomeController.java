package com.mmj.home.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pi4j.device.piface.PiFace;
import com.pi4j.device.piface.PiFaceRelay;
import com.pi4j.device.piface.impl.PiFaceDevice;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.spi.SpiChannel;

/**
 * @author mamejri
 */

@RestController
public class HomeController {

    private static GpioPinDigitalOutput gpioPinDigitalOutput;

    // The Pi-Face controller
    private static PiFace piFace;

    static {
        try {
            piFace = new PiFaceDevice(PiFace.DEFAULT_ADDRESS, SpiChannel.CS0);
        } catch (UnsatisfiedLinkError | IllegalArgumentException | IOException e) {
            System.out.print("Check the connectivity of your PiFace");
        }
    }

    /**
     * Home page
     *
     * @return greating message
     */
    @RequestMapping("/home")
    public String greating() {
        return "Control your Home !";
    }

    /**
     * Change the state of the LED connected to the GPIO_01
     *
     * @return OK if there is no exception
     */
    @RequestMapping("/light")
    public String light() {
        if (gpioPinDigitalOutput == null) {
            try {
                GpioController gpioController = GpioFactory.getInstance();
                gpioPinDigitalOutput = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLed", PinState.LOW);
            } catch (UnsatisfiedLinkError e) {
                return "Platform does not support this driver";
            }
        }

        gpioPinDigitalOutput.toggle();

        return "OK";
    }

    /**
     * Open the Relay with inedx 0 in the PiFace
     *
     * @return String of the state of the Relay if there is no exception
     * @throws IOException exception if Relay not found
     */
    @RequestMapping("/OnRelay1")
    public String turnOnRelay1() throws IOException {
        if (piFace != null) {
            piFace.getRelay(PiFaceRelay.K0).close();
            return "Relay1 On :" + piFace.getRelay(PiFaceRelay.K0).isClosed();
        } else {
            return "Interface PiFace not connected";
        }
    }

    /**
     * Open the Relay with inedx 1 in the PiFace
     *
     * @return String of the state of the Relay if there is no exception
     * @throws IOException exception if Relay not found
     */
    @RequestMapping("/OnRelay2")
    public String turnOnRelay2() throws IOException {
        if (piFace != null) {
            piFace.getRelay(PiFaceRelay.K1).close();
            return "Relay2 On :" + piFace.getRelay(PiFaceRelay.K1).isClosed();
        } else {
            return "Interface PiFace not connected";
        }
    }

    /**
     * Close the Relay with inedx 0 in the PiFace
     *
     * @return String of the state of the Relay if there is no exception
     * @throws IOException exception if Relay not found
     */
    @RequestMapping("/OffRelay1")
    public String turnOffRelay1() throws IOException {
        if (piFace != null) {
            piFace.getRelay(PiFaceRelay.K0).open();
            return "Relay1 On :" + piFace.getRelay(PiFaceRelay.K0).isOpen();
        } else {
            return "Interface PiFace not connected";
        }
    }

    /**
     * Close the Relay with inedx 1 in the PiFace
     *
     * @return String of the state of the Relay if there is no exception
     * @throws IOException exception if Relay not found
     */
    @RequestMapping("/OffRelay2")
    public String turnOffRelay2() throws IOException {
        if (piFace != null) {
            piFace.getRelay(PiFaceRelay.K1).open();
            return "Relay2 On :" + piFace.getRelay(PiFaceRelay.K1).isOpen();
        } else {
            return "Interface PiFace not connected";
        }
    }
}


