
# Image Compression with Automata

A brief description of what this project does and who it's for


## Introduction
This project explores the world of image compression using automata theory, particularly focusing on repetitive images. It's an innovative approach to encode the pixels of an image as strings from a given alphabet, then store the finite automaton that recognizes all the words representing the strings in the image. This approach potentially leads to efficient compression, especially for highly repetitive images
## Features
   1) Decompression: Given a finite automaton, recreate its corresponding image.
2) Compression: Given an image, generate the finite automaton that will reproduce the image.
3) Multi-resolution Images: Encode images in a manner that facilitates viewing at multiple resolutions. 
## How to use?

1) Decompression
`java -cp bin Compress 0 <filepath/filename.txt>`

2) Compression
`java -cp bin src.Compress 0 2 f <filepath/filename.png>`

3) Multi-resolution Decompression
`java -cp bin src.Compress 0 1 t <word len> <filepath/filename.txt>`

4) Multi-resolution Compression
`java -cp bin src.Compress 0 2 t <method> <filepath/filename.png>`


## Decompression (Mode 1)
What it does:

Given a textual description of a finite automaton, this mode produces the corresponding image.

Effect:

Reconstructs the original image from the compressed format (finite automaton).
How it's achieved:

    By reading the finite automaton description from the input text file.
    Processing the automaton to generate the set of input strings it recognizes.
    Mapping the recognized strings to pixel addresses, thus recreating the imag
## Compression (Mode 2)
What it does:

Accepts an image as input and generates the finite automaton that can reproduce the said image.

Effect:

Compresses the image into a finite automaton representation, typically achieving a reduction in storage size for highly repetitive images.
How it's achieved:

    The image is processed to identify patterns or repetitions.
    These patterns are encoded as strings from a given alphabet.
    The finite automaton is then constructed to recognize these encoded strings, resulting in the compressed format of the image.
## Multi-resolution Decompression
What it does:

It takes a finite automaton as input and produces an image at a specified resolution.

Effect:

Generates an image with a specific resolution from a finite automaton that has information about the image at multiple resolutions.
How it's achieved:

    The finite automaton is processed considering the specified word length (resolution).
    The recognized strings are mapped to pixel addresses to recreate the image at the desired resolution.
## Multi-resolution Compression
What it does:

Compresses an image into a finite automaton that can reproduce the image at multiple resolutions.

Effect:

Creates a versatile compressed format that can reproduce the image at various resolutions, potentially providing more efficient storage for multi-resolution use-cases.
How it's achieved:

    The image is processed to identify patterns at different resolutions.
    These patterns are encoded, and the finite automaton is constructed to recognize these encodings.
    Techniques like "Sierpinski triangle magnification", "Checkerboard", and "Reduce" are employed to simulate or achieve multi-resolution effects.
## Authors

- [Italo Marini](https://github.com/Italo115)

