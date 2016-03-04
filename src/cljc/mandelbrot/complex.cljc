(ns mandelbrot.complex)

(defn z [x y] [x y])

(defn add [z1 z2]
  (let [[x1 y1] z1
        [x2 y2] z2]
    [(+ x1 x2) (+ y1 y2)]))

(defn mult [z1 z2]
  (let [[x1 y1] z1
        [x2 y2] z2]
    [(- (* x1 x2)
        (* y1 y2))
     (+ (* y1 x2)
        (* y2 x1))]))

(defn abs [z]
  (let [[x y] z]
    (#?(:clj Math/sqrt :cljs js/Math.sqrt) (+ (* x x) (* y y)))))
