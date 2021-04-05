# Koneked
Project for ENEL 400 at the University of Calgary

**Ideas:**
1. ~~Use the PDM microphone on Arduino Nano 33 BLE Sense to record to a WAV file~~ (*Unable to get it to work, but here are some notes*)
    a. ~~Probably need to implement a FIR Decimation filter (https://www.theunterminatedstring.com/probing-pdm/)~~ (*only necessary if need to convert from PDM to PCM*)
    b. It seems like the PDM microphone on the Arduino Nano 33 BLE Sense outputs audio in PCM (https://forum.arduino.cc/index.php?topic=678154.0)
    c. Important to understand how digital audio works (https://manual.audacityteam.org/man/digital_audio.html)
    d. It seems like this has been implemented on the Teensyduino (https://forum.arduino.cc/index.php?topic=550869.0)
    e. Audio recording with Arduino seems to be a pain (https://forum.arduino.cc/index.php?topic=561135.0)
    f. Located in the Arduino folder
2.  Implemented 2 Tensorflow models for STT - Notes:
    a. Works offline
    b. Very low accurracy
    c. 1st Model Resource: https://create.arduino.cc/projecthub/alankrantas/eloquenttinyml-easier-voice-classifier-on-nano-33-ble-sense-ebb81e
    d. 2nd Model Resource: https://eloquentarduino.github.io/2020/08/better-word-classification-with-arduino-33-ble-sense-and-machine-learning/
    e. Both are located in the Arduino folder
3. Implemented SST using python - Notes:
    a. Located in the Python folder
    b. Language: Uses online APIs - currently working with the Microsoft Bing Voice Recognition (https://pypi.org/project/SpeechRecognition/)
    c. Offline_SST: Uses offline API - currently working with Mozilla DeepSpeech (https://github.com/mozilla/DeepSpeech)
    d. **Issue:** Both Require WAV file --> **Solution:** Buy a microphone wired to the Arduino (https://www.youtube.com/watch?v=7Hn4UFi9wvs&ab_channel=GreatScott%21)
    
**Features:**
Virtual Python Environment:
 - Installed with required libraries to run all python scripts in this repository
 - https://packaging.python.org/tutorials/installing-packages/

**Resources:**
 - Writing python files in Android: https://www.youtube.com/watch?v=m_vV5ZKBuNg&ab_channel=ProgrammingFever
 - Working with audio files: https://realpython.com/python-speech-recognition/#working-with-audio-files
 - Implement Python and Java together: https://chaquo.com/chaquopy/doc/current/android.html
 - Allows designing of TIR filters quickly: http://t-filter.engineerjs.com/

The latter allows writing python code with Java but requires NDK tools (Files->Settings->Appearance & Behavior->System Settings->Android SDK):
![image](https://user-images.githubusercontent.com/14882398/110557510-f3c83e80-80fd-11eb-89fa-8b3753e74047.png)
