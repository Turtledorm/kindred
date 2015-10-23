__author__ = 'Pedro'
import pygame
import os
import numpy


class SoundController(object):
    def __init__(self, frequency=22050, size=-16, channels=2, buffersize=4096):
        """
        Initialize the sound controller. The parameters inputted are for
        the mixer configuration
        :param frequency:
        :param size:
        :param channels:
        :param buffersize:
        :return:
        """
        # Initialize the mixer and set the mute option to false
        pygame.mixer.init(frequency, size, channels, buffersize)
        self.mute = False

        # Music represents whether the music is muted,
        # Loops and start are the settings of the music
        # (loops = number of times the music should loop,
        #  start = start second of the music)
        self.music = True
        self.loops = -1
        self.start = 0.0

        # Sound effects represent if the sounds are enabled
        # sounds list is a list of all the loaded sounds
        self.sound_effects = True
        self.sound_list = {}

        # Number of channels
        self.number_of_channels = channels
        self.named_channels = 0
        self.channels = {}

    ''' Mixer related methods '''

    def quit(self):
        """
        Quit the mixer
        :return:
        """
        self.music = False
        self.sound_effects = False
        self.music_playing = False
        self.mute = True
        pygame.mixer.quit()

    def stop(self):
        """
        Stop all sounds and music
        :return:
        """
        self.mute = True
        pygame.mixer.stop()
        pygame.mixer.music.stop()

    def resume(self):
        """
        Resume to play sounds
        :return:
        """
        self.mute = False
        if self.music:
            self.music_play(loops=self.loops, start=self.start)

    def pause(self):
        """
        Pause all sounds and music
        :return:
        """
        self.mute = True
        pygame.mixer.pause()

    def unpause(self):
        """
        Unpause the mixer
        :return:
        """
        self.mute = False
        pygame.mixer.unpause()

    ''' Sounds related methods '''

    def sound_load(self, name, obj=None, file_extension=".ogg", folder="Sounds"):
        """
        Load the sound
        :param name: name of the sound
        :param obj: Object that the sound can be loaded from
        :param file_extension: Is the extension of the sound
        :param folder: Folder containing the sound
        :return:
        """
        # If the sound is not loaded yet
        if name not in self.sound_list:
            # If the name is a string
            if obj is None:
                # We look for the file and load the sound
                s = pygame.mixer.Sound(os.path.join(folder, name + file_extension))
            else:
                # Else we load the sound from the passed object
                s = pygame.mixer.Sound(obj)

            # We add the sound to the list of loaded sounds
            self.sound_list[name] = numpy.empty(2, dtype=object)
            self.sound_list[name][0] = s

    def sound_play(self, name, loops=0, maxtime=0, fade_ms=0):
        """
        Play the sound in any available channel
        :param name: Name of the sound
        :param loops: number of times the sound should be played
        :param maxtime: maxtime the sound should play
        :param fade_ms: will make the sound start playing in 0 volume and fade up
        :return:
        """
        if self.sound_effects and not self.mute:
            # We should check if the sound is related to a channel
            # and play it in that channel
            if self.sound_list[name][1] is None:
                self.sound_list[name][0].play(loops, maxtime, fade_ms)
            else:
                self.channels[self.sound_list[name][1]].play(self.sound_list[name][0], loops, maxtime, fade_ms)

    def sound_stop(self, name):
        """
        Stop the sound
        :param name: Name of the sound
        :return:
        """
        self.sound_list[name][0].stop()

    def sound_fadeout(self, name, time):
        """
        Fade out the sound
        :param name: Name of the sound
        :param time: Fade out time
        :return:
        """
        self.sound_list[name][0].fadeout(time)

    def sound_set_volume(self, name, volume):
        """
        Set volume of the sound
        :param name: Name of the sound
        :param volume: Float volume to be set
        :return:
        """
        self.sound_list[name][0].set_volume(volume)

    def sound_get_volume(self, name):
        """
        Get volume of the sound
        :param name: Name of the sound
        :return: An float containing the sound volume
        """
        return self.sound_list[name][0].get_volume()

    def sound_num_channels(self, name):
        """
        Get the number of channels where the sound is playing
        :param name: Name of the sound
        :return: An integer with the number of channels where the sound is playing
        """
        return self.sound_list[name][0].num_channels()

    def sound_get_length(self, name):
        """
        Get the length of the Sound
        :param name: Name of the sound
        :return: An float containing the seconds
        """
        return self.sound_list[name][0].get_length()

    def sound_get_raw(self, name):
        """
        Return a bytestring copy of the Sound samples
        :param name: Name of the sound
        :return: bytes
        """
        return self.sound_list[name][0].get_raw()

    ''' Channels related methods '''

    def set_channel(self, name):
        """
        Set a name for a channel
        :param name: Name of the channel
        :return:
        """
        # We can get a channel if there is non named channels
        if self.named_channels < self.number_of_channels:
            # A channel is composed of its channel object and its list of sounds
            self.channels[name] = pygame.mixer.Channel(self.named_channels)
            self.named_channels += 1

    def add_sound_to_channel(self, channel_name, name, obj=None, file_extension=".ogg", folder="Sounds", create=True):
        """
        We load a sound and them add it to the selected channel
        :param channel_name: Name of the channel
        :param name: Name of the sound
        :return:
        """
        # First we load the sound if necessary
        if name not in self.sound_list:
            self.sound_load(name, obj, file_extension, folder)

        # Then we check to see if there is a channel with that name
        if channel_name in self.channels:
            # And we add the sound to that channel
            self.sound_list[name][1] = channel_name
        else:
            # If not we create the channel
            if create:
                self.set_channel(channel_name)

                # And we add the sound to that channel
                self.sound_list[name][1] = channel_name

    def channel_stop(self, name):
        """
        Stop the selected channel
        :param name: channel name
        :return:
        """
        self.channels[name].stop()

    def channel_pause(self, name):
        """
        Pause the selected channel
        :param name: channel name
        :return:
        """
        self.channels[name].pause()

    def channel_unpause(self, name):
        """
        Unpause selected channel
        :param name: channel name
        :return:
        """
        self.channels[name].unpause()

    def channel_fadeout(self, name, time):
        """
        Fade channel out
        :param name: selected channel
        :param time: time of fadeout
        :return:
        """
        self.channels[name].fadeout(time)

    def channel_set_volume(self, name, volume):
        """
        Set volume to channel
        :param name: name of selected channel
        :param volume: volume to be set
        :return:
        """
        self.channels[name].set_volume(volume)

    def channel_get_volume(self, name):
        """
        Get volume of selected channel
        :param name: channel name
        :return: float with channel volume
        """
        return self.channels[name].get_volume()

    def channel_get_busy(self, name):
        """
        Checks if channels has a sound playing
        :param name: channel name
        :return: boolean
        """
        return self.channels[name].get_busy()

    def channel_get_sound(self, name):
        """
        Get the sound object that is currently playing
        :param name: selected channel
        :return: pygame sound object
        """
        return self.channels[name].get_sound()

    def channel_queue(self, name, sound_name, add=False):
        """
        Queue sound in channel
        :param name: name of channel
        :param sound_name: name of sound
        :param add: boolean if we should add the sound to the channel
        :return:
        """
        self.channels[name].queue(self.sound_list[sound_name])
        if add:
            self.add_sound_to_channel(name, sound_name)

    def channel_get_queue(self, name):
        """
        Return any sound that is queued
        :param name: name of the channel
        :return: pygame sound object
        """
        return self.channels[name].get_queue()

    ''' Music related methods '''

    def music_load(self, name, folder="Sounds"):
        """
        Loads a music from the sounds folder
        :param name: Name of the music to load
        :return:
        """
        pygame.mixer.music.load(os.path.join(folder, name))

    def music_play(self, loops=-1, start=0.0):
        """
        Plays the loaded music
        :return:
        """
        if self.music and not self.mute:
            self.loops = loops
            self.start = start
            self.music_playing = True
            pygame.mixer.music.play(loops, start)

    def load_and_play(self, name, folder="Sounds", loops=-1, start=0.0, volume=1.0):
        """
        Load music and start playing it
        :param name: Music name
        :param folder: Folder containing music
        :param loops: Number of times music should play
        :param start: Start time of music
        :param volume: Volume of the music
        :return:
        """
        self.music_stop()
        self.music_load(name, folder)
        self.music_rewind()
        self.music_play(loops, start)
        self.music_set_volume(volume)

    def music_rewind(self):
        """
        Rewind the current loaded music
        :return:
        """
        pygame.mixer.music.rewind()

    def music_stop(self):
        """
        Stop the current loaded music
        :return:
        """
        self.music_playing = False
        pygame.mixer.music.stop()

    def music_pause(self):
        """
        Pause the current loaded music
        :return:
        """
        self.music_playing = False
        pygame.mixer.music.pause()

    def music_unpause(self):
        """
        Unpause the current loaded music
        :return:
        """
        if self.music:
            self.music_playing = True
            pygame.mixer.music.unpause()

    def music_fadeout(self, time):
        """
        Makes the music goes to a fade out effect
        :return:
        """
        pygame.mixer.music.fadeout(time)

    def music_set_volume(self, volume):
        """
        Set volume of the current loaded music
        :param volume: Volume of the music
        :return:
        """
        pygame.mixer.music.set_volume(volume)

    def music_get_volume(self):
        """
        Get the music volume
        :return: float
        """
        return pygame.mixer.music.get_volume()

    def music_get_busy(self):
        """
        Gets whether there is music playing
        :return: bool
        """
        return pygame.mixer.music.get_busy()

    def music_queue(self, name, folder="Sounds"):
        """
        Queue music new music
        :param name: name of music
        :param folder: folder of music
        :return:
        """
        pygame.mixer.music.queue(os.path.join(folder, name))
