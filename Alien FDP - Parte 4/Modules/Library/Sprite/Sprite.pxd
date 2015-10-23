# -*- coding: utf-8 -*-
"""
Created on Thu Jul 10 17:30:05 2014

@author: Pedro
"""

cdef class Sprite:
    
    cdef add(self, groups)

    cdef inline update(self, args)

    cdef inline draw(self, screen)

    cdef kill(self)