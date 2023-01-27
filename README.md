# envi4j
Simple Java library for reading ENVI hyper-spectral images.

## Documentation

* [Header files](https://www.l3harrisgeospatial.com/docs/enviheaderfiles.html)
* [Optional Header Information](https://www.l3harrisgeospatial.com/docs/enteroptionalheaderinformation.html)
* [Image files](https://www.l3harrisgeospatial.com/docs/enviimagefiles.html)
* [Classification files](https://www.l3harrisgeospatial.com/docs/enviclassificationfiles.html)


## Supported file formats

* Interleave: BSQ, BIL, BIP
* Data type (byte order: L=little endian, B=big endian):

  * float32 (LB): 4
  * float64 (LB): 5
  * int16 (LB): 2
  * int32 (LB): 3
  * int64 (LB): 14
  * uint8 (LB): 1 
  * uint16 (L): 12
