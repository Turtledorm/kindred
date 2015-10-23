__author__ = 'Pedro'
import pygame
import os
from pygame.locals import *

from Constants import MAIN_CLOCK
from Constants import FPS
from BasicGame import BasicGame
from Variables import Variables
from SoundController import SoundController
from Game import Game


class GameController(BasicGame):
    def __init__(self):
        """
        The game controller correspond to the class that
        defines all possible stages or menus to be displayed
        to the player. Not only that, it controls the order
        in which those shall be displayed
        :return:
        """
        # We call the basic game constructor with the sound controller
        super().__init__(SoundController())

        # We open the opening image
        self.open_image = pygame.image.load(os.path.join("Images", "opening.png"))
        self.open_image = pygame.transform.scale(self.open_image, (1280, 720)).convert()

        # And the game over image
        self.game_over = pygame.image.load(os.path.join("Images", "gameover.png"))
        self.game_over = pygame.transform.scale(self.game_over, (1280, 720)).convert()

        # And the win image
        self.win_image = pygame.image.load(os.path.join("Images", "win.png"))
        self.win_image = pygame.transform.scale(self.win_image, (1280, 720)).convert()

        # Set the background to be the open image
        self.background = self.open_image

        # We load the first music
        self.sound_controller.music_load("ost_opening.mp3")
        self.sound_controller.music_set_volume(0.5)

        # We set a channel for the laser and one for the energy ball
        self.sound_controller.add_sound_to_channel("laser", "sfx_laser_1", file_extension=".wav")
        self.sound_controller.add_sound_to_channel("energy", "sfx_energy_ball", file_extension=".wav")

        # We load the game
        self.game = Game(self.sound_controller)

    def main(self):
        """
        Constrols the actions to be taken during the game
        :return:
        """
        # We start the sound and wait for the player to press enter
        self.sound_controller.music_play(loops=-1)
        self.wait_to_press_enter()

        while True:
            # Then we go to the main function of the game
            # But first we fade_out the playing song and
            # queue the main_theme music
            self.sound_controller.load_and_play("ost_maintheme.mp3", volume=0.3)
            if self.game.main():
                # If the player won we set the background to victory
                self.background = self.win_image
                self.sound_controller.stop()
                self.sound_controller.resume()
                self.sound_controller.load_and_play("ost_win.mp3", volume=0.3)
            else:
                # We set the background to lost
                self.background = self.game_over
                self.sound_controller.stop()
                self.sound_controller.resume()
                self.sound_controller.load_and_play("ost_gameover.wav", loops=1, volume=0.3)

            # We wait the player to press enter to play again
            self.wait_to_press_enter()

            # And we reset the game
            self.game.reset()

    def wait_to_press_enter(self):
        """
        Makes the game wit to press enter
        :return:
        """
        # Once both has been checked we put the background
        # on the screen and update the game
        Variables.screen.fill((0, 0, 0))
        Variables.screen.blit(self.background, (0, 0))

        while True:
            # First we check for events
            for event in pygame.event.get():
                # The quit event
                BasicGame.quit_event_handler(event)

                # The music event
                self.stop_music_event_handler(event)

                # And pressing enter event
                if event.type == KEYDOWN:
                    if event.key == K_RETURN:
                        return

            # Finally we update the screen
            self.update_screen(MAIN_CLOCK, FPS)