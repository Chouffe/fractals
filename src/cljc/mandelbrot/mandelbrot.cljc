(ns mandelbrot.mandelbrot
  (:require [mandelbrot.utils :as utils]
            [mandelbrot.complex :as c]))

(defn mandelbrot? [z]
  (let [max-iter 20]
    (loop [k 1
           m (iterate #(c/add z (c/mult % %)) (c/z 0 0))]
      (if (and (> max-iter k) (< (c/abs (first m)) 2))
        (recur (inc k) (rest m))
        (= max-iter k)))))

(defn ^:private mandelbrot-n-iter-aux
  [z u n-max k]
  (cond
    (> (c/abs u) 2) [false k]
    (>= k n-max) [true k]
    :else (recur z (c/add z (c/mult u u)) n-max (inc k))))

(defn ^:private mandelbrot-n-iter [z]
  (mandelbrot-n-iter-aux z (c/z 0 0) 20 1))

(defn ^:private mandelbrot-value [range-x range-y]
  (for [y range-y
        x range-x]
    (let [[in-set? k] (mandelbrot-n-iter (c/z x y))]
      (if in-set? 0 k))))

(defn mandelbrot-data [width height zoom x-start y-start]
  (let [x-step (utils/x-step-value zoom)
        y-step (utils/y-step-value zoom)]
    (partition width (mandelbrot-value (utils/x-range x-start x-step width)
                                       (utils/x-range y-start y-step height)))))
