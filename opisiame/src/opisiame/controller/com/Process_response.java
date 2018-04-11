/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.com;

import com.rapplogic.xbee.XBeePin;
import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.RemoteAtRequest;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.wpan.IoSample;
import com.rapplogic.xbee.api.wpan.RxResponseIoSample;

/**
 *
 * @author Sandratra
 */
public class Process_response extends Thread {

    XBeeResponse response;

    private XBee xbee = new XBee();

    public Process_response(XBeeResponse response, XBee xbee_coordinator) {
        this.response = response;
        this.xbee = xbee_coordinator;
    }

    String led_yellow = "D7";
    String led_green = "D5";
    String led_red = "D4";

    @Override
    public void run() {
        if (response.getApiId() == ApiId.RX_64_IO_RESPONSE) {
            RxResponseIoSample ioSample = (RxResponseIoSample) response;

            XBeeAddress64 address_remote = (XBeeAddress64) ioSample.getSourceAddress();

            try {
                for (IoSample sample : ioSample.getSamples()) {

                    if (!ioSample.containsAnalog()) {
                        if (!sample.isD2On()) { // bouton : rouge
                            switch_on_led(led_red, address_remote);
                            switch_off_led(led_red, address_remote);
                        }
                        if (!sample.isD1On()) {
                            switch_on_led(led_yellow, address_remote);
                            switch_off_led(led_yellow, address_remote);
                        }
                        if (!sample.isD0On()) {
                            switch_on_led(led_green, address_remote);
                            switch_off_led(led_green, address_remote);
                        }
                    }
                }
            } catch (XBeeException e) {
                e.printStackTrace();
            }
        }
    }

    public void switch_on_led(String led_id, XBeeAddress64 address_remote) throws XBeeException {

        RemoteAtRequest request_led_on = new RemoteAtRequest(address_remote, led_id, new int[]{XBeePin.Capability.DIGITAL_OUTPUT_HIGH.getValue()});
        xbee.sendAsynchronous(request_led_on);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        // xbee.clearResponseQueue();
    }

    public void switch_off_led(String led_id, XBeeAddress64 address_remote) throws XBeeException {
        RemoteAtRequest request_led_off = new RemoteAtRequest(address_remote, led_id, new int[]{XBeePin.Capability.DIGITAL_OUTPUT_LOW.getValue()});
        xbee.sendAsynchronous(request_led_off);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        // xbee.clearResponseQueue();
    }
}
