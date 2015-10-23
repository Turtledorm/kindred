from Sprite cimport Sprite


cdef class Character(Sprite):
    cdef public int max_vx, max_vy, vx, vy
    cdef readonly float num_x, num_y
    cdef public int alive

    cdef void set_vy(self, int vy)

    cdef void set_vx(self, int vx)

    cdef void set_alive(self, int alive)

    cdef move(self, float dt)

    cdef inline kill(self)
