__author__ = 'Pedro'

import os
import pygame
import random
from pygame.locals import KEYDOWN
try:
    from Variables import Variables
    from Constants import MAIN_CLOCK
    from Constants import FPS
    from Constants import DT
    from Sprite import Sprite
    from game_functions import load_image_sequence
    from BasicGame import BasicGame
    from LayerController import LayerController
    from Counter import Counter
    from Text import Text
    from Player import Player
    from MotherShip import MotherShip
    from Generator import Generator
    from Shield import Shield
    from EnemyLevel1 import EnemyLevel1
    from EnemyLevel2 import EnemyLevel2
    from EnemyLevel3 import EnemyLevel3
    from EnergyBall import EnergyBall
except ImportError:
    pass


class Game(BasicGame):
    def __init__(self, sound_controller):
        super().__init__(sound_controller)

        # Pause boolean
        self.pause = False

        # Opens the background image
        self.background = pygame.image.load(os.path.join('Images', 'background.png')).convert()

        # Creates the layer controller
        self.layer_controller = LayerController()

        # We create five layers
        self.shield_layer = []
        self.main_enemy_layer = []
        self.enemy_layer = []
        self.player_layer = []
        self.info_layer = []

        # Add layers
        self.add_layers()

        # Load image resources for game
        self.enemy1 = pygame.image.load(os.path.join("Images", "enemy_1.png")).convert_alpha()
        self.enemy2 = pygame.image.load(os.path.join("Images", "enemy_2.png")).convert_alpha()
        self.enemy3 = pygame.image.load(os.path.join("Images", "enemy_3.png")).convert_alpha()
        self.up = pygame.image.load(os.path.join("Images", "up.png")).convert_alpha()
        self.energy_ball = load_image_sequence("Images", "energy_ball", 4)
        self.generator = load_image_sequence("Images", "generator", 4)

        # Creates the player, shield, mother ship and generators
        self.player = Player((self.player_layer, ))
        self.mother_ship = MotherShip(Variables.screen, (self.main_enemy_layer, ))
        self.shield = Shield(Variables.screen, (self.shield_layer, ))
        self.generator1 = Generator(self.generator, self.shield, Variables.screen, (self.main_enemy_layer, ))
        self.generator2 = Generator(self.generator, self.shield, Variables.screen, (self.main_enemy_layer, ), pos=1)

        # We also put a text informing the character level
        self.level_text = Text("Level: ", pygame.font.SysFont("Bernard MT Condensed", 28),
                               (self.info_layer, ), color=(255, 255, 255))

        # We load the level image
        self.level_image = pygame.image.load(os.path.join("Images", "player_level.png"))
        self.level_image = pygame.transform.scale(self.level_image, (20, 20)).convert_alpha()

        # And create a level list
        self.level_list = []
        self.level_list.append(Sprite((self.level_text.rect.width, 0), self.level_image, (self.info_layer, )))

        # Counter for wave, boolean checking if the ships
        # should be add. Number of ships added for the wave
        # counter between adding ships for waves
        self.wave_cont = Counter(5)
        self.should_add = False
        self.ships_added = 0
        self.new_ship_cont = Counter(9*DT)

        # Counter for adding an energy ball
        self.energy_cont = Counter(10)

    def add_layers(self):
        """
        Add the used layer for the layer controller
        :return:
        """
        self.layer_controller.add("shield", self.shield_layer)
        self.layer_controller.add('main enemy', self.main_enemy_layer)
        self.layer_controller.add('enemy', self.enemy_layer)
        self.layer_controller.add('player', self.player_layer)

    def main(self):
        """
        Main method of the game
        :return: Boolean representing if the user has won the game
        """
        while True:
            # First we check for events
            self.check_for_events()

            # Update the counters of wave and energy ball
            self.update_counters()

            # Then we update all elements of the game
            self.update_game()

            # Draw the game on the screen
            self.draw_game()

            # Update screen
            self.update_screen(MAIN_CLOCK, FPS)

            # Finally we check to see if the player has been killed
            if not self.player.alive:
                return False

            # Or if the mother ship has been destroyed
            if not self.mother_ship.alive:
                return True

    def check_for_events(self):
        """
        Check for events in the game
        :return:
        """
        for event in pygame.event.get():
            # Check for the event of quitting the game
            BasicGame.quit_event_handler(event)

    def update_counters(self):
        """
        Update counters of the game
        :return:
        """
        # If we are not adding new ships
        if not self.should_add:
            # We update the counter for a new wave
            if self.wave_cont.update(DT):
                # If the counter reaches its limit we should
                # add a new wave, therefore we set the boolean
                # to true
                self.should_add = True

        # If we are adding ships at a new wave
        else:
            # We update the new ship counter
            if self.new_ship_cont.update(DT):
                # If the new ship counter reaches its limit
                # we should add a new ship. Therefore we increment
                # the ships added and call the new ship method
                self.ships_added += 1
                self.new_ship()

                # Once we reach the limit of new ships in a wave
                if self.ships_added == 5:
                    # We cancel the adding state and reset
                    # the ships added
                    self.should_add = False
                    self.ships_added = 0

        if self.energy_cont.update(DT):
            EnergyBall(self.energy_ball, self.sound_controller, (self.enemy_layer, ))

    def new_ship(self):
        """
        Creates a new random enemy ship
        :return:
        """
        new = random.choice([1, 2, 3])
        if new == 1:
            EnemyLevel1(self.enemy1, self.player.rect.y, (self.enemy_layer, ))
        elif new == 2:
            EnemyLevel2(self.enemy2, (self.enemy_layer, ))
        else:
            EnemyLevel3(self.enemy3, (self.enemy_layer, ))

    def update_game(self):
        """
        Update all instances inside the game
        :return:
        """
        self.layer_controller.update_layer_list(['main enemy', 'enemy', 'player'], self)

    def draw_game(self):
        """
        Draw the game
        :return:
        """
        # Clean the screen with the black color
        Variables.screen.fill((0, 0, 0))

        # Blit the background
        Variables.screen.blit(self.background, (0, 0))

        # Draw the sprites layer
        self.layer_controller.draw_layers(Variables.screen)

    def reset(self):
        """
        Reset the game
        :return:
        """
        # We reset the enemy layer
        self.layer_controller.clear_layer('enemy')
        self.layer_controller.clear_layer('player')

        # We restore the main enemies
        self.generator1.reset((self.main_enemy_layer, ))
        self.generator2.reset((self.main_enemy_layer, ))
        self.mother_ship.reset((self.main_enemy_layer, ))

        # We reset the player
        self.player.level = 1
        self.player.life = 1
        self.player.alive = True
        self.player.rect.topleft = (200, 350)
        self.player.add((self.player_layer, ))

        # And restart the counters
        self.wave_cont.restart()
        self.new_ship_cont.restart()
        self.energy_cont.restart()
        self.should_add = False
        self.ships_added = 0

        # We add the shield to the shield layer
        self.shield.add((self.shield_layer, ))