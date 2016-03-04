(ns mandelbrot.views
  (:require [clojure.string :as s]
            [re-frame.core :as re-frame]
            [reagent.core :refer [create-class]]
            [mandelbrot.canvas :as canvas]
            [mandelbrot.utils :as utils]))

(defn send-canvas-property-value [v prop-kw]
    (when-not (s/blank? v)
      (re-frame/dispatch [:set-canvas-property prop-kw v])))

(defn slider [param value min max on-change-fn]
  [:input.slider-input
   {:type "range" :value value :min min :max max
    :style {:width "100%"}
    :on-mouse-up #(re-frame/dispatch [:change-fractal-colors])
    :on-change on-change-fn}])

(defn color-component [[r g b :as color]]
  [:div.color-picker
   {:style {:background-color (str "rgb(" r ", " g ", " b ")")}}])

(defn color-slider [color-kw [r g b :as color]]
  (let [color-mask (case color-kw
                     :red [r 0 0]
                     :green [0 g 0]
                     :blue [0 0 b])
        color-value (apply max color-mask)]
    [:div.color-slider
     (color-component color-mask)
     [slider color-kw color-value 0 255
      (fn [e] (re-frame/dispatch [:change-color color-kw
                                  (-> e .-target .-value js/parseInt)]))]
     [:div.clearfix]]))

(defn canvas []
  (let [zoom (re-frame/subscribe [:zoom])
        x-start (re-frame/subscribe [:x-start])
        y-start (re-frame/subscribe [:y-start])]
    (fn [] [:canvas#fractal
            {:on-click
             (fn [e]
               (let [cursor (canvas/cursor-position-on-canvas e)
                     position (canvas/pos cursor @x-start @y-start @zoom)
                     [new-xstart new-ystart :as n] (canvas/middle-point->top-left position @zoom canvas/width canvas/height)]
                 (send-canvas-property-value new-xstart :x-start)
                 (send-canvas-property-value new-ystart :y-start)
                 (re-frame/dispatch [:draw-fractal])))}])))

(defn fractal-zoom [zoom-type]
  (assert (get #{:in :out} zoom-type))
  (re-frame/dispatch [:zoom zoom-type])
  (re-frame/dispatch [:draw-fractal]))

(defn zoom-control []
  (let [zoom (re-frame/subscribe [:zoom])]
    (fn []
      [:div
       [:div.btn-group
        [:button.btn.btn-default {:on-click #(fractal-zoom :out)} "-"]
        [:button.btn.btn-default {:on-click #(fractal-zoom :in)} "+"]]])))

(defn color-control []
  (let [palette-end (re-frame/subscribe [:palette-end])]
    (fn []
      [:div.color-control
       [:div.main-color
        (color-component @palette-end)
        [:div.clearfix]]
       [:div.sliders
        (color-slider :red @palette-end)
        (color-slider :green @palette-end)
        (color-slider :blue @palette-end)]])))

(defn main-panel []
  (fn []
    [:div.row
     [:div.col-xs-6.mandelbrot-container
      [canvas]
      [zoom-control]]
     [:div.col-xs-6
      [color-control]]]))
