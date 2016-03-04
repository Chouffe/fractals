(ns mandelbrot.db
  (:require [mandelbrot.utils :as utils]
            [mandelbrot.canvas :as canvas]
            [mandelbrot.mandelbrot :as m]))

(def default-db {:zoom 2
                 :palette-n 175
                 :palette-start [0 0 0]
                 :palette-end [255 255 255]
                 :fractal-data nil
                 :fractal-fn (comp utils/transpose (partial m/mandelbrot-data canvas/width canvas/height))
                 :x-start -2.8
                 :y-start 1.8})

