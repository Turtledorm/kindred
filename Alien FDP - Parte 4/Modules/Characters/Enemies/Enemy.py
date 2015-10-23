__author__ = 'Pedro'

import pygame
import random
from Character import Character
from Constants import DT


class Enemy(Character):
    def __init__(self, name, y, velocity, image, life, groups):
        # We randomly select an x position
        x = random.randint(800, 1050)

        # We call the constructor of the character
        super().__init__(name, (x, y), velocity, image, groups)

        # Life of the enemy
        self.max_life = self.life = life

        # Damage that enemy can cause
        self.damage = min(self.life, 3)

        # All the enemies moves to the left
        self.vx = -self.max_vx

    def update(self, game):
        """
        Update method an enemy
        :return:
        """
        self.move(DT)

    def chase_player(self, player, dt):
        """
        Calculate the velocity to chase the player
        :param player: Player instance
        :param dt: Time interval between frames
        :return:
        """
        # We calculate the distance between the player
        # and the enemy
        dx = player.rect.x - self.rect.right
        dy = player.rect.y - self.rect.y

        # We choose a vy velocity for the enemy
        self.vy = 0
        if dy != 0:
            d = 1 if dy > 0 else -1
            self.vy = min(abs(dy/dt), self.max_vy)*d if dx < -self.rect.width else 0

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
            self.kill()

    def hit(self, damage):
        """
        Make the enemy suffer damage and kills it
        if necessary
        :param damage: Damage inflicted
        :return:
        """
        self.life -= damage
        if self.life <= 0:
            self.kill()

    def reset(self, groups):
        """
        Reset enemy configurations
        :param groups: Groups to where the enemy belongs
        :return:
        """
        self.life = self.max_life
        self.alive = True
        self.add(groups)

