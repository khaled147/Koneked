# NOTE: Do not need PyAudio library
# Resource: https://realpython.com/python-speech-recognition/#working-with-audio-files
# TODO: Convert audio data from arduino to wav source file to send to main

import speech_recognition as s_r

dit = 1000
dah = dit*3

def cha_to_vib(x):
    vib1 = 0
    vib2 = 0
    vib3 = 0

    if x == ' ':
        vib3 = dit
        vib3 = dit
    elif x == 'e' or x == 'E':
        vib1 = dit
        vib3 = dit
    elif x == 'i' or x == 'I':
        vib2 = dit
        vib3 = dit
    elif x == 't' or x == 'T':
        vib1 = dit
        vib1 = dit
        vib3 = dit
    elif x == 'a' or x == 'A':
        vib2 = dit
        vib2 = dit
        vib3 = dit
    elif x == 'n' or x == 'N':
        vib1 = dah
        vib3 = dit
    elif x == 's' or x == 'S':
        vib2 = dah
        vib3 = dit
    elif x == 'd' or x == 'D':
        vib1 = dit
        vib1 = dah
        vib3 = dit
    elif x == 'h' or x == 'H':
        vib2 = dit
        vib2 = dah
        vib3 = dit
    elif x == 'm' or x == 'M':
        vib1 = dah
        vib1 = dit
        vib3 = dit
    elif x == 'r' or x == 'R':
        vib2 = dah
        vib2 = dit
        vib3 = dit
    elif x == 'u' or x == 'U':
        vib1 = dit
        vib1 = dit
        vib1 = dit
        vib3 = dit
    elif x == 'b' or x == 'B':
        vib2 = dit
        vib2 = dit
        vib2 = dit
        vib3 = dit
    elif x == 'f' or x == 'F':
        vib1 = dah
        vib1 = dit
        vib1 = dit
        vib3 = dit
    elif x == 'g' or x == 'G':
        vib2 = dah
        vib2 = dit
        vib2 = dit
        vib3 = dit
    elif x == 'k' or x == 'K':
        vib1 = dit
        vib1 = dit
        vib1 = dit
        vib1 = dit
        vib3 = dit
    elif x == 'l' or x == 'L':
        vib2 = dit
        vib2 = dit
        vib2 = dit
        vib2 = dit
        vib3 = dit
    elif x == 'v' or x == 'V':
        vib1 = dah
        vib1 = dah
        vib3 = dit
    elif x == 'w' or x == 'W':
        vib2 = dah
        vib2 = dah
        vib3 = dit
    elif x == '5':
        vib1 = dit
        vib1 = dah
        vib1 = dit
        vib3 = dit
    elif x == 'c' or x == 'C':
        vib2 = dit
        vib2 = dah
        vib2 = dit
        vib3 = dit
    elif x == 'o' or x == 'O':
        vib1 = dit
        vib1 = dit
        vib1 = dah
        vib3 = dit
    elif x == 'p' or x == 'P':
        vib2 = dit
        vib2 = dit
        vib2 = dah
        vib3 = dit
    elif x == 'x' or x == 'X':
        vib1 = dah
        vib1 = dit
        vib1 = dit
        vib1 = dit
        vib3 = dit
    elif x == 'z' or x == 'Z':
        vib2 = dah
        vib2 = dit
        vib2 = dit
        vib2 = dit
        vib3 = dit
    elif x == '4':
        vib1 = dit
        vib1 = dah
        vib1 = dit
        vib1 = dit
        vib3 = dit
    elif x == '6':
        vib2 = dit
        vib2 = dah
        vib2 = dit
        vib2 = dit
        vib3 = dit
    elif x == 'j' or x == 'J':
        vib1 = dit
        vib1 = dit
        vib1 = dah
        vib1 = dit
        vib3 = dit
    elif x == 'q' or x == 'Q':
        vib2 = dit
        vib2 = dit
        vib2 = dah
        vib2 = dit
        vib3 = dit
    elif x == 'y' or x == 'Y':
        vib1 = dit
        vib1 = dit
        vib1 = dit
        vib1 = dah
        vib3 = dit
    elif x == '3':
        vib2 = dit
        vib2 = dit
        vib2 = dit
        vib2 = dah
        vib3 = dit
    elif x == '7':
        vib1 = dah
        vib1 = dah
        vib1 = dit
        vib3 = dit
    elif x == '2':
        vib2 = dah
        vib2 = dah
        vib2 = dit
        vib3 = dit
    elif x == '8':
        vib1 = dah
        vib1 = dit
        vib1 = dah
        vib3 = dit
    elif x == '1':
        vib2 = dah
        vib2 = dit
        vib2 = dah
        vib3 = dit
    elif x == '9':
        vib1 = dit
        vib1 = dah
        vib1 = dah
        vib3 = dit
    elif x == '0':
        vib2 = dit
        vib2 = dah
        vib2 = dah
        vib3 = dit
 #   print(vib1, vib2, vib3, x)

def main(stream):
    CHUNK = 4096
    CHANNELS = 1
    RATE = 16000
    RECORD_SECONDS = 1
    WAVE_OUTPUT_FILENAME = "test.wav"

    frames = []
    for i in range(0, int(RATE / CHUNK * RECORD_SECONDS)):
        data = stream.read(CHUNK)
        print(data)
        frames.append(data)

    wf = wave.open(WAVE_OUTPUT_FILENAME, 'wb')
    wf.setnchannels(CHANNELS)
    wf.setframerate(RATE)
    wf.writeframes(b''.join(frames))
    wf.close()
    return wf
    # = s_r.Recognizer()
    # Implements Google Web Speech API
    #test = r.recognize_google(audio)
    #return test #to print voice into text

#for cha in test:
#    cha_to_vib(cha)


#my_mic = s_r.Microphone(device_index=1) #my device index is 1, you have to put your device index
#with my_mic as source:
#    print("Say now!!!!")
#    r.adjust_for_ambient_noise(source) #reduce noise
#    audio = r.listen(source) #take voice input from the microphone

#f = open("test.txt", 'rt')
#lines = f.readlines()
#for line in lines:
#    for cha in line:
#        cha_to_vib(cha)