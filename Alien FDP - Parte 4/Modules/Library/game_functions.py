__author__ = 'Pedro'
import pygame
import os


def load_image_sequence(image_folder, base_image_name, number_of_images, file_extension=".png"):
    """
    Load a given sequence of images in the refered folder
    :param image_folder: String of the folder that contains the image
    :param base_image_name: Base image name
    :param number_of_images: number of images to be loaded
    :return: An list with the images
    """
    return [pygame.image.load(os.path.join(image_folder, base_image_name + str(i) + file_extension)).convert_alpha()
            for i in range(1, number_of_images+1)]