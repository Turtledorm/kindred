__author__ = 'Pedro'

from Constants import DT
from Character import Character


class Laser(Character):
    generator_count = 0

    def __init__(self, position, image, level, groups):
        super().__init__("Laser", position, (900, 0), image, groups)

        # Level of the laser
        self.level = level

        # The laser only moves on the x direction to the right
        self.vx = self.max_vx

    def update(self, game):
        """
        Update the laser
        :param game: Game instance
        :return:
        """
        # First we move the laser
        self.move(DT)

        # Then we check to see if it collided with an enemy
        self.collide_with_enemy(game)

        # We check to see if there is a collision with the shield
        if len(game.shield_layer) > 0:
            self.collide_with_shield(game)

    def collide_with_enemy(self, game):
        """
        Checks to see if the laser collided with an enemy
        :param enemies: Enemies game list
        :return:
        """
        for enemy in game.enemy_layer + game.main_enemy_layer:
            # If we got an collision
            if enemy.rect.colliderect(self.rect):
                # We kill the laser
                self.kill()

                # And decrease the enemy life
                enemy.hit(self.level)

                # We also check to see if the enemy is dead
                if not enemy.alive:
                    # If the enemy is a generator we should increase the
                    # generator count. When it reachs 2 we should also
                    # kill the shield
                    if enemy.name == "Generator":
                        Laser.generator_count += 1
                        if Laser.generator_count == 2:
                            game.shield_layer[0].kill()

    def collide_with_shield(self, game):
        """
        Checks to see if there is a collision with shield
        :param game: Game instance
        :return:
        """
        if self.rect.colliderect(game.shield_layer[0].rect):
            self.kill()