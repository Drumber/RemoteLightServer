# RemoteLightServer
A Server for the [RemoteLight LED control software](https://github.com/Drumber/RemoteLight) to control addressable LED strips with Raspberry Pi.  
Various digital LED strips can be controlled with **all Raspberry Pi versions** thanks to Matthew Lewis' [diozero library](https://github.com/mattjlewis/diozero).

## Supported LED Strips
- WS2812(B)
- WS2811
- SK6812
- TMI1829
- Apa102
- WS2801

I have only tested WS2812B LED strips with a Raspberry Pi 3 B+. However, due to their similarity to the WS2812 LEDs, WS2811 and SK6812 LED strips should work as well.  
[According to Matthew Lewis](https://github.com/mattjlewis/diozero/blob/56d27aacba9a5c4f2636111ac46120a367d4d9cd/diozero-ws281x-java/src/main/java/com/diozero/ws281xj/StripType.java#L40) Apa102 and TMI1829 LED strips should also work.

## Connecting the LED strip to the Raspberry Pi
### PWM LED strip (WS2812/11)
Connect a LED strip with a single data line to the Raspberry Pi:  

#### Preparation
> See also here for more details: https://github.com/Drumber/RemoteLight#raspberry-pi

First you need to disable the onboard audio.  
Create a file `sudo nano /etc/modprobe.d/snd-blacklist.conf` and add this line `blacklist snd_bcm2835`.  
Then open the configuration file `sudo nano /boot/config.txt` and comment following line out:
```
# Enable audio (loads snd_bcm2835)
dtparam=audio=on
```
Restart the Pi: `sudo reboot`

#### Wiring
LED Strip   | Raspberry Pi  | Power supply
----------  | ------------- | ------------
5V          |               | **+**
DIN         | GPIO18        |
GND         | GND           | **-**

### SPI LED strip (WS2801/Apa102)
Connect a LED strip with two data lines to the Raspberry Pi:

#### Preparation
Enable SPI in the Raspberry Pi config:
1. `sudo raspi-config`
2. `5. Interfacing Options`
3. `P4 SPI`
4. `Yes`, `Ok`, `Finish`

#### Wiring
LED Strip   | Raspberry Pi  | Power supply
----------  | ------------- | ------------
5/12V          |               | **+**
CK / CLK    | GPIO11 (Pin23)|
SI / DI     | GPIO10 (Pin19)|
GND         | GND           | **-**


## Software Installation
> [Java](https://www.java.com) is required. Raspberry Pi installation: `sudo apt-get install oracle-java8-jdk`

> See also here: https://github.com/Drumber/RemoteLight#raspberry-pi

Download the [RemoteLight.jar](https://github.com/Drumber/RemoteLightClient/releases/latest) and [RemoteLightServer.jar](https://github.com/Drumber/RemoteLightServer/releases/latest).  
Upload the RemoteLightServer.jar to your Raspberry Pi (e.g. via [WinSCP](https://winscp.net/eng/download.php)) and start it with `sudo java -jar RemoteLightServer-pre0.2.0.2.jar`.  
Stop the server by typing `end` or pressing `Ctrl + C`.  
It should have created a 'config.properties' file in the same folder. You can edit the file with `sudo nano config.properties` if you want.

```
strip_type=ws2812   //Possible strip types: SK6812[W,_RGBW,_RBGW,_GRBW,_GBRW,_BRGW,_BGRW]
                                            WS2811[_RGB,_RBG,_GRB,_GBR,_BRG,_BGR], WS2812, WS2801, Apa102
gpio_pin=18
led_number=auto     //Any number > 0, Auto (= Recognizes the setting you have set in RemoteLight)
save_logs=true
```
