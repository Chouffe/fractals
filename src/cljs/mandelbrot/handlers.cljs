(ns mandelbrot.handlers
   (:require [re-frame.core :as re-frame]
             [mandelbrot.utils :as utils]
             [mandelbrot.canvas :as canvas]
             [mandelbrot.db :as db]))

(re-frame/register-handler :initialize-db (fn  [_ _] db/default-db))

(re-frame/register-handler
   :draw-fractal
   (fn [{:keys [fractal-fn zoom palette-n palette-start palette-end x-start y-start] :as db} _]
      (let [fractal-data (fractal-fn zoom x-start y-start)
            palette (utils/palette-range palette-start palette-end palette-n)]
         (->> fractal-data
              (utils/map-matrix (utils/value->color-fn [0 20] palette))
              (canvas/draw-rgb-matrix! (canvas/get-context "fractal")))
         (assoc db :fractal-data fractal-data))))

(re-frame/register-handler
   :change-fractal-colors
   (fn [{:keys [palette-start palette-end palette-n fractal-data] :as db} _]
      (let [palette (utils/palette-range palette-start palette-end palette-n)]
         (->> fractal-data
              (utils/map-matrix (utils/value->color-fn [0 20] palette))
              (canvas/draw-rgb-matrix! (canvas/get-context "fractal"))))
      db))

(re-frame/register-handler
   :set-canvas-property
   (fn [db [_ prop-kw v]]
      (assert (get #{:x-start :y-start :zoom :palette-start :palette-end} prop-kw))
      (assoc db prop-kw v)))

(re-frame/register-handler
   :zoom
   (fn [db [_ zoom-type]]
      (case zoom-type
         :in (update db :zoom inc)
         :out (update db :zoom dec)
         db)))

(re-frame/register-handler
   :change-color
   (fn [db [_ color-kw color-value]]
      (assert (get #{:red :blue :green} color-kw))
      (assert (<= 0 color-value 255))
      (assoc-in db [:palette-end
                    (case color-kw
                       :red 0
                       :green 1
                       :blue 2)] color-value)))
