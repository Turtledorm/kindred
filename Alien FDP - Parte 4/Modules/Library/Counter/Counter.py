__author__ = 'Pedro'


class Counter(object):
    def __init__(self, max):
        self.cont = 0
        self.max = max
        self.init = True

    def update(self, dt):
        """
        Update count by dt
        :param dt: Time interval
        :return: True if counter finished, false otherwise
        """
        if self.init:
            self.cont += dt
            if self.cont > self.max:
                self.cont = 0
                return True
            return False

    def restart(self):
        """
        Restart the counter
        :return:
        """
        self.init = True
        self.cont = 0

    def pause(self):
        """
        Pause the counter
        :return:
        """
        self.init = False

    def stop(self):
        """
        Stop counter
        :return:
        """
        self.init = False
        self.cont = 0