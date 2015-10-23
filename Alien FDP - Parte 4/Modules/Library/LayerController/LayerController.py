__author__ = 'Pedro'


class LayerController(object):
    """
    The layer control is an object used to set
    different lists of objects that make the draw
    layers in the game
    """
    def __init__(self):
        self.layers_list = []
        self.layers_name = []
        self.number_of_layers = 0

    def add(self, name, layer):
        """
        Add a new layer at the bottom
        :param name:
        :param layer:
        :return:
        """
        # We add the layer to the list
        self.layers_list.insert(0, layer)

        # We add the layer name
        self.layers_name.insert(0, name)

        # We increase the number of layers
        self.number_of_layers += 1

    def remove(self, name):
        """
        Remove the selected layer
        :param name: Name of the layer
        :return:
        """
        # We retrieve the index of the element
        element = self.layers_name.index(name)

        # We remove the layer in that index
        self.layers_list.pop(element)

        # We remove the layer name
        self.layers_name.remove(name)

        # We decrease the number of layers
        self.number_of_layers -= 1

    def clear_layer(self, name):
        """
        Empty a given layer
        :param name: name of the layer
        :return:
        """
        element = self.layers_name.index(name)
        for sprite in self.layers_list[element].copy():
            sprite.kill()

    def move_layer_to_beginning(self, name):
        """
        Move selected layer to the beginning
        :param name: Name of the layer
        :return:
        """
        element = self.layers_name.index(name)
        layer = self.layers_list.pop(element)
        self.layers_list.append(layer)
        layer_name = self.layers_name.pop(element)
        self.layers_name.append(layer_name)

    def move_layer_to_end(self, name):
        """
        Move selected layer to the end
        :param name: Name of the layer
        :return:
        """
        element = self.layers_name.index(name)
        layer = self.layers_list.pop(element)
        self.layers_list.insert(0, layer)
        layer_name = self.layers_name.pop(element)
        self.layers_name.insert(0, layer_name)

    def move_layer_up(self, name):
        """
        Move the layer one position up
        :param name: Name of the layer
        :return:
        """
        element = self.layers_name.index(name)
        layer = self.layers_list.pop(element)
        self.layers_list.insert(element + 1, layer)
        layer_name = self.layers_name.pop(element)
        self.layers_name.insert(element + 1, layer_name)

    def move_layer_down(self):
        element = self.layers_name.index(name)
        layer = self.layers_list.pop(element)
        self.layers_list.insert(element - 1, layer)
        layer_name = self.layers_name.pop(element)
        self.layers_name.insert(element - 1, layer_name)

    def update_layer_list(self, ls, game):
        """
        Update a given set of layer
        :param ls: List of layers
        :param game: Game instance
        :return:
        """
        for layer in ls:
            i = self.layers_name.index(layer)
            for obj in self.layers_list[i]:
                obj.update(game)

    def update_all_layers(self, game):
        """
        Update all objects inside the layers
        :param game: Game instance
        :return:
        """
        for layer in self.layers_list:
            for obj in layer:
                obj.update(game)

    def draw_layers(self, screen):
        """
        Draw all layer on the screen
        :param screen: Screen of the game
        :return:
        """
        for layer in self.layers_list:
            for obj in layer:
                obj.draw(screen)