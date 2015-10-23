__author__ = 'Pedro'
import pygame
from Sprite import Sprite


class Character(Sprite):
    def __init__(self, name, position, velocity, image, groups):
        """
        A character consists of an element of tha game with
        an image that is capable of move and collide with
        other elements of the game
        :param name: String containing the name of the character
        :param position: Initial position of the character
        :param velocity: Tuple with the maximum velocity of character
        :param image: Initial image of the character
        :param groups: Groups to where the character belong
        :return:
        """
        super().__init__(position, image, groups)

        # Initialize the character name
        self.name = name

        # We set the character to be alive
        self.alive = True

        # Velocity of the character
        self.max_vx = velocity[0]
        self.max_vy = velocity[1]
        self.vx = self.vy = 0
        self.num_x = self.num_y = 0

    def move(self, dt):
        """
        Move the character
        :param dt: time interval between frames
        :return:
        """
        # Since rect only accept integer values we take
        # an int movement in the x direction
        move_x = int(self.vx*dt)

        # We add the real part of the number to the num_x attribute
        self.num_x += self.vx*dt - move_x

        # If the abs value of num_x is bigger then 1
        if abs(self.num_x) >= 1:
            # We add 1 (- or +) to the rect x
            move_x += int(self.num_x)

            # And decrease num_x by 1
            self.num_x -= 1*(self.num_x/abs(self.num_x))

        # We do the same for the y direction
        move_y = int(self.vy*dt)
        self.num_y += self.vy*dt - move_y
        if abs(self.num_y) >= 1:
            move_y += int(self.num_y)
            self.num_y -= 1*(self.num_y/abs(self.num_y))

        # And we move the rect
        self.rect.x += move_x
        self.rect.y += move_y

    def kill(self):
        """
        Kills the character
        :return:
        """
        self.alive = False
        super().kill()
