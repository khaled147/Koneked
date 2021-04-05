#Tutorial: https://medium.com/@MrMaran/speech-to-text-prediction-preambling-of-deepspeech-models-d8a8581aac47#:~:text=%E2%80%9CDeepSpeech%20is%20an%20open%20source,to%20make%20the%20implementation%20easier.%E2%80%9D
#To install packages and set up python environment: https://packaging.python.org/tutorials/installing-packages/
#To install pyAudio on windows: https://stackoverflow.com/questions/53866104/pyaudio-failed-to-install-windows-10/53866322
#deepspeech releases: https://github.com/mozilla/DeepSpeech/releases/

import pyaudio
from deepspeech import Model
import scipy.io.wavfile as wav
import wave

MODEL_PATH = 'deepspeech-0.9.3-models.pbmm'
SCORER_PATH = 'deepspeech-0.9.3-models.scorer'


def record_voice_stream(stream_file_name):
    stream_format = pyaudio.paInt16  # Sampling size and format
    no_of_channels = 1  # Number of audio channels
    sampling_rate = 16000  # Sampling rate in Hertz
    frames_count = 1024  # Number of frames per buffer
    record_seconds = 5

    stream = pyaudio.PyAudio()

    stream_data = stream.open(format=stream_format,
                              channels=no_of_channels,
                              rate=sampling_rate,
                              input=True,
                              frames_per_buffer=frames_count)
    print("Please speak to record your voice")
    frames = [stream_data.read(frames_count) for i in range(0, int(sampling_rate / frames_count * record_seconds))]
    stream_data.stop_stream()
    stream_data.close()
    stream.terminate()

    wave_file = wave.open(stream_file_name, 'wb')
    wave_file.setnchannels(no_of_channels)
    wave_file.setsampwidth(stream.get_sample_size(stream_format))
    wave_file.setframerate(sampling_rate)
    wave_file.writeframes(b''.join(frames))
    wave_file.close()
    print(f'Voice stream file {stream_file_name} is created')


def predict_speech_to_text(stream_file):
    # Initialize the model
    speech_model = Model(MODEL_PATH)

    # Enable language scorer to improve the accuracy
    speech_model.enableExternalScorer(SCORER_PATH)
    # You can play with setting the model Beam Width, Scorer language model weight and word insertion weight

    # Use scipy to covert wav file into numpy array
    _, audio = wav.read(stream_file)
    return speech_model.stt(audio)


if __name__ == '__main__':
    output_stream_file = 'speech_stream.wav'
    record_voice_stream(output_stream_file)
    print('Start of text prediction')
    print(f'DeepSpeech predicted text: {predict_speech_to_text(output_stream_file)}')