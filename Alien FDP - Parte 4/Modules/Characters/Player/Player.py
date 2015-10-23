__author__ = 'Pedro'

import pygame
from game_functions import load_image_sequence
from Variables import Variables
from Constants import DT
from Character import Character
from Counter import Counter
from Laser import Laser


class Player(Character):
    def __init__(self, groups):
        # Images of the player
        self.level_image = load_image_sequence("Images", "player", 4)

        # We call the constructor of the character
        super().__init__("Yamato", (200, 350), (300, 300), self.level_image[0], groups)

        # Images of the laser
        self.laser_images = load_image_sequence("Images", "laser", 4)

        # Life of the player
        self.life = 1

        # Level of the player
        self.level = 1

        # Laser counter
        self.laser_counter = Counter(10*DT)
        self.laser_counter.pause()

        # Last pressed boolean variable
        self.last_pressed = False

    def update(self, game):
        """
        Update method of the player
        :return:
        """
        # We first set the velocity to be 0
        self.vx = self.vy = 0

        # We deal with possible key pressing
        self.deal_with_keys(game)

        # Then we make sure to move the player
        self.move(DT)

        # We also check to see if the player didn't go across
        # the shield
        if len(game.shield_layer) > 0:
            if self.rect.right > game.shield_layer[0].rect.x:
                self.rect.right = game.shield_layer[0].rect.x

        # Then we check to see if there is a collision with
        # an enemy
        self.collide_with_enemy(game)

    def deal_with_keys(self, game):
        """
        Deal with possible key pressing events
        :return:
        """
        # Gets all the keys that are pressed
        keys = pygame.key.get_pressed()

        # Deal with the press of the shooting key
        self.shoot_key(keys, game)

        # Event of pressing the up movement key
        if keys[Variables.UP]:
            # If he pressed up we make his velocity be
            # -max_vy
            self.vy -= self.max_vy

        # Event of pressing the down movement key
        if keys[Variables.DOWN]:
            # If he pressed down we make his velocity be
            # max_vy
            self.vy += self.max_vy

        # Event of pressing the left movement key
        if keys[Variables.LEFT]:
            # If he pressed left we make his velocity be
            # -max_vx
            self.vx -= self.max_vx

        # Event of pressing the right movement key
        if keys[Variables.RIGHT]:
            # If he pressed right we make his velocity be
            # max_vx
            self.vx += self.max_vx

    def shoot_key(self, keys, game):
        """
        Deals with the shooting key press
        :param keys: Dictionary of keys pressed
        :param game: Game instance
        :return:
        """
        """

        # Option 1 - Have the player hold the key to shoot mutiple projectiles

        # Update the laser counter
        if self.laser_counter.update(DT):
            self.laser_counter.stop()

        # Event of pressing the shooting key
        if keys[Variables.SHOOT] and not self.laser_counter.init:
            # If he pressed the shooting key we should create a shoot
            # projectile if we didn't create one recently
            Laser(self.rect.center, self.laser_images[self.level - 1], self.level,  (game.player_layer, ))
            self.laser_counter.restart()

        # """

        # Option 2 - Have the player press the key
        # multiple times
        # Event of pressing the shooting key
        if keys[Variables.SHOOT] and not self.last_pressed:
            # If he pressed the shooting key we should create a shoot
            # projectile if we didn't create one recently
            game.sound_controller.sound_play("sfx_laser_1")
            Laser(self.rect.center, self.laser_images[0], 1,  (game.player_layer, ))
            self.laser_counter.restart()
            self.last_pressed = True

        # We set the last pressed to the current state of the key
        self.last_pressed = keys[Variables.SHOOT]

        # """

    def move(self, dt):
        """
        Moves the player
        :param dt: time interval between frames
        :return:
        """
        # Call the superclass move method
        super().move(dt)

        # Makes sure the player doesn't leave the boundaries
        # of the screen
        if self.rect.x < 0:
            self.rect.x = 0
        elif self.rect.right > Variables.screen.get_width():
            self.rect.right = Variables.screen.get_width()
        if self.rect.y < 0:
            self.rect.y = 0
        elif self.rect.bottom > Variables.screen.get_height():
            self.rect.bottom = Variables.screen.get_height()

    def collide_with_enemy(self, game):
        """
        Checks for collisions with enemies
        :param game: Game instance
        :return:
        """
        # For each enemy in the enemy layer
        for enemy in game.enemy_layer:
            # If the player collides with an enemy
            if self.rect.colliderect(enemy.rect):
                # We kill the enemy
                enemy.kill()

                # And decrease the player life
                self.life -= enemy.damage
                if self.life <= 0:
                    self.alive = False

        # We also check for a collision with the mother ship
        for enemy in game.main_enemy_layer:
            if enemy.name == "Mother Ship":
                if self.rect.colliderect(enemy.rect):
                    # If a collision occurs we kill the player
                    self.alive = False

    def change_image(self):
        """
        Changes the player image and rect according to the player level
        :return:
        """
        self.image = self.level_image[self.level - 1]
        self.rect.width = self.image.get_width()
        self.rect.height = self.image.get_height()



