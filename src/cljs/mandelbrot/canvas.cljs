(ns mandelbrot.canvas
  (:require [monet.canvas :as canvas]
            [mandelbrot.utils :as utils]))

(def width 300)
(def height 150)

(defn draw-rgb-matrix! [ctx matrix]
  (doseq [i (range (count matrix))
          j (range (count (first matrix)))]
    (-> ctx
        (canvas/fill-style (utils/color-value->str (get-in matrix [i j])))
        (canvas/fill-rect {:x i :y j :w 1 :h 1}))))

(defn get-context [canvas-id]
  (-> (.getElementById js/document canvas-id) (canvas/get-context "2d")))

(defn draw-fractal! [fractal-fn zoom x-start y-start palette]
  (->> (fractal-fn zoom x-start y-start)
       (utils/map-matrix (utils/value->color-fn [0 20] palette))
       (draw-rgb-matrix! (canvas/get-context "fractal"))))

(defn cursor-position-on-canvas [e]
  (let [canvas (.getElementById js/document "fractal")
        x (- (.-pageX e) (.-offsetLeft canvas))
        y (- (.-pageY e) (.-offsetTop canvas))]
    [x y]))

(defn pos [[x y] x-start y-start zoom]
  (let [x-step (utils/x-step-value zoom)
        y-step (utils/y-step-value zoom)]
    [(+ x-start (* x x-step))
     (+ y-start (* y y-step))]))

(defn middle-point->top-left [[x y :as middle-point] zoom width height]
  (let [x-step (utils/x-step-value zoom)
        y-step (utils/y-step-value zoom)]
    [(- x (* x-step (quot width 2)))
     (- y (* y-step (quot height 2)))]))
