__author__ = 'Pedro'

import random
from Enemy import Enemy
from Constants import DT
from Counter import Counter


class EnergyBall(Enemy):
    def __init__(self, images, sound_controller, groups):
        # The level 1 enemy only has x velocity
        velocity = (300, 300)

        # We call the constructor of the character
        super().__init__("Energy Ball", random.randint(20, 651), velocity, images[0], 10, groups)

        # Sound name for the energy ball
        self.sound_name = "sfx_energy_ball"

        # We set an image counter
        self.image_list = images
        self.image_counter = Counter(2*DT)
        self.image_number = 0
        self.image_max = 4

        # We start playing the energy ball sound
        self.sound = sound_controller
        self.sound.sound_set_volume(self.sound_name, 0.7)
        self.sound.sound_play(self.sound_name, loops=-1)

    def update(self, game):
        """
        Update method for the enemy level 2
        :param game:
        :return:
        """
        # We make the ball chase the player
        self.chase_player(game.player, DT)

        # We move the ball around
        self.move(DT)

        # And we select an image to it
        if self.image_counter.update(DT):
            self.image_number = self.image_number + 1 if self.image_number + 1 < self.image_max else 0
            self.image = self.image_list[self.image_number]

    def kill(self):
        super().kill()
        self.sound.sound_stop(self.sound_name)