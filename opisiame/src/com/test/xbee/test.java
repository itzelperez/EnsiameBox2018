package com.test.xbee;

import com.rapplogic.xbee.XBeePin;
import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.ErrorResponse;
import com.rapplogic.xbee.api.RemoteAtRequest;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.wpan.IoSample;
import com.rapplogic.xbee.api.wpan.RxResponseIoSample;
import com.rapplogic.xbee.examples.ApiAtExample;
import java.util.logging.Level;
import javafx.application.Platform;

public class test {

    private final static Logger log = Logger.getLogger(ApiAtExample.class);

    private XBee xbee = new XBee();

    String led_yellow = "D7";
    String led_green = "D5";
    String led_red = "D4";

    /*
    DIO0 boutton vert
    DIO1 boutton gris
    DIO2 boutton rouge
    DIO3 boutton bleu
     */
    public test() {

        try {

            xbee.open("COM4", 9600);
            while (true) {
                xbee.clearResponseQueue();
                XBeeResponse response = xbee.getResponse();

                if (response.isError()) {
                    log.info("response contains errors", ((ErrorResponse) response).getException());
                    continue;
                }
                
                ProcessResponse processResponse = new ProcessResponse(response);
                processResponse.start();
            }
        } catch (Exception e) {
            log.error(e);
        }

    }

    public void switch_on_led(String led_id, XBeeAddress64 address_remote) throws XBeeException {

        RemoteAtRequest request_led_on = new RemoteAtRequest(address_remote, led_id, new int[]{XBeePin.Capability.DIGITAL_OUTPUT_HIGH.getValue()});
        xbee.sendAsynchronous(request_led_on);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }
        // xbee.clearResponseQueue();
    }

    public void switch_off_led(String led_id, XBeeAddress64 address_remote) throws XBeeException {
        RemoteAtRequest request_led_off = new RemoteAtRequest(address_remote, led_id, new int[]{XBeePin.Capability.DIGITAL_OUTPUT_LOW.getValue()});
        xbee.sendAsynchronous(request_led_off);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }
        // xbee.clearResponseQueue();
    }

    public static void main(String[] args) throws XBeeException {
       // PropertyConfigurator.configure("log4j.properties");
        new test();
    }

    class ProcessResponse extends Thread {

        XBeeResponse response;

        public ProcessResponse(XBeeResponse response) {
            this.response = response;
        }

        @Override
        public void run() {
            if (response.getApiId() == ApiId.RX_64_IO_RESPONSE) {
                RxResponseIoSample ioSample = (RxResponseIoSample) response;

                System.out.println("iosampe : " + ioSample.toString());

                System.err.println("iosample ::: " + ioSample.toString());

                XBeeAddress64 address_remote = (XBeeAddress64) ioSample.getSourceAddress();

                System.err.println("address : " + ioSample.getSourceAddress());
                System.err.println("address64 : " + address_remote);

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
    }

}
