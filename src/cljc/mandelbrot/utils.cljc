(ns mandelbrot.utils)

(defn map-matrix [f matrix]
  (mapv (partial mapv f) matrix))

(defn transpose [matrix]
  (apply mapv vector matrix))

(defn x-range
  ([start step] (cons start (lazy-seq (x-range (+ step start) step))))
  ([start step n] (take n (x-range start step))))

(defn gradient [start end n]
  (assert (pos? n))
  (map int (range start end (/ (- end start) n))))

(defn palette-range
  [[r1 g1 b1 :as start-color] [r2 g2 b2 :as end-color] n]
  (mapv (fn [r g b] [r g b])
        (gradient r1 r2 n)
        (gradient g1 g2 n)
        (gradient b1 b2 n)))

(defn value->color-fn [xs palette]
  (let [min-value (apply min xs)
        max-value (apply max xs)
        n-colors (count palette)]
    (fn [value]
      (get palette (quot (* n-colors (- value min-value))
                         (- max-value min-value))))))

(defn color-value->str
  [[r g b :as color]]
  (str "rgb(" r ", " g ", " b ")"))

(defn str->color [string]
  (let [[r g b :as color] (drop 1 (re-find #"rgb\((\d+),\s*(\d+),\s*(\d+).*\)" string))]
    (mapv #?(:clj #(Integer. %) :cljs #(js/parseInt %)) color)))

(defn x-step-value [zoom]
  (/ 0.0315 zoom))

(defn y-step-value [zoom]
  (/ -0.05 zoom))

