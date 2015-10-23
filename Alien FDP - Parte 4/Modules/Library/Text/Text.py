__author__ = 'Pedro'
from Sprite import Sprite


class Text(Sprite):
    def __init__(self, text, font, groups, color=(0, 0, 0), position=(0, 0)):
        """
        Create a new font render <- return None
        """
        self.font = font
        self.text = text
        self.color = color
        super().__init__(position, font.render(self.text, False, self.color), groups)

    def change_text(self, text, color="default"):
        """
        Change the font render's text. <- return None
        """
        if color == "default":
            color = self.color
        self.color = color
        self.text = text
        self.image = self.font.render(self.text, False, self.color)

    def get_width(self):
        """
        Returns the width of the font rendered image. <- return int
        """
        return self.image.get_width()

    def get_height(self):
        """
        Returns the height of the font rendered image. <- return int
        """
        return self.image.get_height()