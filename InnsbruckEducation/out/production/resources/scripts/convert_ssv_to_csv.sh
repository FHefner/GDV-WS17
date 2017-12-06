#!/bin/bash

# First replace commas with points (in geo-locations)
sed -i "s/,/\./g" $1
# Afterwards replace all semicolons with commas
sed -i "s/\;/\,/g" $1
