(ns mandelbrot.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(re-frame/register-sub :zoom (fn [db] (reaction (:zoom @db))))
(re-frame/register-sub :x-start (fn [db] (reaction (:x-start @db))))
(re-frame/register-sub :y-start (fn [db] (reaction (:y-start @db))))
(re-frame/register-sub :palette-start (fn [db] (reaction (:palette-start @db))))
(re-frame/register-sub :palette-end (fn [db] (reaction (:palette-end @db))))
(re-frame/register-sub :palette-n (fn [db] (reaction (:palette-n @db))))
(re-frame/register-sub :fractal-fn (fn [db] (reaction (:fractal-fn @db))))
