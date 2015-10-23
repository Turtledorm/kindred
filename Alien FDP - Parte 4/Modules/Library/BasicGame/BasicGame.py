__author__ = 'Pedro'
import pygame
import sys
from pygame.locals import *


class BasicGame(object):
    def __init__(self, sound_controller):
        # Set the sound controller
        self.sound_controller = sound_controller

    def main(self):
        """
        Main Function of the game
        :return:
        """
        pass

    @staticmethod
    def quit_event_handler(event):
        """
        Makes the game wit to press enter
        :return:
        """
        if event.type == QUIT:
            pygame.quit()
            sys.exit()

    def stop_music_event_handler(self, event):
        """
        Deals with the event of pressing the m key
        :param event: Event object
        :return:
        """
        if event.type == KEYDOWN:
            if event.key == ord('m'):
                if self.sound_controller.mute:
                    self.sound_controller.resume()
                else:
                    self.sound_controller.stop()

    def update_screen(self, clock, fps):
        """
        Update the game
        :return:
        """
        # Tick the clock
        clock.tick(fps)

        # Update the screen
        pygame.display.flip()