add_library("unfolding")
add_library("log4j-1.2.15")
# from de.fhpotsdam.unfolding import UnfoldingMap as UnfoldingMap
import de.fhpotsdam.unfolding as Unfolding
#import de.fhpotsdam.unfolding.geo as Geo
#import de.fhpotsdam.unfolding.utils as Utils

m = Unfolding.UnfoldingMap(this)

def setup():
    global m
    size(800,600)
    background(0)
    print(type(this))
    # m = Unfolding.UnfoldingMap(this, "Test!")

def draw():
    global m
    # text("Hallo!",0,0)
    background(0)
    # m.draw()