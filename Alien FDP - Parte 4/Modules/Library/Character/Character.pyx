import pygame
from Sprite cimport Sprite

# Class of the character
# agents that can operate a few actions
cdef class Character(Sprite):
    def __cinit__(self):
        # And the velocity
        self.vy = 0
        self.vx = 0

        # numbX and numbY are the floating point rest of a determinated movement
        # of the character. See the move method
        self.num_x = 0
        self.num_y = 0

        # A boolean to represent the state of the character
        self.alive = 1

    # Getter and setter methods
    cdef void set_vy(self, int vy):
        self.vy = vy

    cdef void set_vx(self, int vx):
        self.vx = vx

    cdef void set_alive(self, int alive):
        self.alive = alive


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

        # Call the sprite constructor
        super(Character, self).__init__(position, image, groups)

        # Initialize the character name
        self.name = name

        # Velocity of the character
        self.max_vx = velocity[0]
        self.max_vy = velocity[1]


    cpdef move(self, float dt):
        """
        Move the character
        :param dt: time interval between frames
        :return:
        """
        # First we do the x movement, taking to account the fact
        # that the rectangles cant use floats
        if (self.vx * dt) - (int)(self.vx * dt) > 0:
            # Verify if there is any floating point to add to self.numb
            self.num_x += (self.vx * dt) - (int)(self.vx * dt)

            # then we verify if numb is greater than 1 in module,
            # and, if it is, we add this to the rectangle
            if self.num_x > 1 or self.num_x < -1:
                if self.num_x > 0:
                    self.num_x -= 1
                    self.rect.x += 1
                else:
                    self.num_x += 1
                    self.rect.x -= 1

        # We do the same thing in the y direction
        if (self.vy * dt) - (int)(self.vy * dt) > 0:
            # Verify if there is any floating point to add to self.numb
            self.num_y += (self.vy * dt) - (int)(self.vy * dt)

            # then we verify if numb is greater than 1 in module,
            # and, if it is, we add this to the rectangle
            if self.num_y > 1 or self.num_y < -1:
                if self.num_y > 0:
                    self.num_y -= 1
                    self.rect.y += 1
                else:
                    self.num_y += 1
                    self.rect.y -= 1

        # At last we move the character image
        self.rect.x += (int)(self.vx * dt)
        self.rect.y += (int)(self.vy * dt)

    cpdef kill(self):
        """
        Kills the character
        :return:
        """
        self.alive = 0
        super().kill()