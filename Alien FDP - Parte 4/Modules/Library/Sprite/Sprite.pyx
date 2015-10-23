import pygame
from Sprite cimport Sprite


cdef class Sprite:
    """simple base class for visible game objects

    pygame.sprite.Sprite(*groups): return Sprite

    The base class for visible game objects. Derived classes will want to
    override the Sprite.update() method and assign Sprite.image and Sprite.rect
    attributes.  The initializer can accept any number of Group instances that
    the Sprite will become a member of.

    When subclassing the Sprite class, be sure to call the base initializer
    before adding the Sprite to Groups.

    """

    def __init__(self, position, imagegroups):
        # The groups the sprite is in
        self.g = []
        self.add(groups)

        # The image of the sprite
        self.image = image

        # The rect of the sprite
        self.rect = pygame.rect.Rect(position, self.image.get_size())

    cpdef add(self, groups):
        """add the sprite to groups

        Sprite.add(*groups): return None

        Any number of Group instances can be passed as arguments. The
        Sprite will be added to the Groups it is not already a member of.

        """
        for group in groups:
            if self not in group:
                group.append(self)
                self.g.append(group)

    def update(self, *args):
        """method to control sprite behavior

        Sprite.update(*args):

        The default implementation of this method does nothing; it's just a
        convenient "hook" that you can override. This method is called by
        Group.update() with whatever arguments you give it.

        There is no need to use this method if not using the convenience
        method by the same name in the Group class.

        """
        pass

    cpdef kill(self):
        """remove the Sprite from all Groups

        Sprite.kill(): return None

        The Sprite is removed from all the Groups that contain it. This won't
        change anything about the state of the Sprite. It is possible to
        continue to use the Sprite after this method has been called, including
        adding it to Groups.

        """
        for c in self.g.copy():
            try:
                c.remove(self)
                self.g.remove(c)
            except:
                pass

    def draw(self, screen):
        """
        Draw the sprite on the screen
        :param screen: screen of the game
        :return:
        """
        screen.blit(self.image, self.rect)