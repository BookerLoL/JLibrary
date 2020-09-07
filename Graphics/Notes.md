# Notes based on Real-Time Rendering 4th Edition
-Create 3d imagary, create 2d images of world 
- Rate of images displayed (FPS / Hz)

## Vocabulary
- FPS: Frames (num images) per second 
    - rate for frame or avg performance over time
- Hz: Hertz (1/seconds)
    - frequency of update
- Model coordinates: coordinates of obj
- API: Application Programming Interface
- View Space: What camera can see after view transformation on world space
- Shading: effect of light on material
- Canonical view volume: Unit cube
- Frustum: view volume of truncated pyramid with rectangular base
- Screen coordinates: x and y 
- Window coordinates: include z 
- Texturing: gluing images onto object
- ROP: Raster operations (pipeline) / render output unit
- Double buffering: rendering takes off screen in back buffer, contents swapped to front for viewers
- GPU: Graphics Processing Unit

## Rendering Pipeline
- 3D scene -> 2D image
- **4 main stages** 
    - Application
    - Geometry Processing
    - Rasterization
    - Pixel processing
- **Application Stage**
    - mostly CPU, maybe in GPU (computer shader)
    - get rendering primitives ready for next stage
        - handle input, acceleration algos, collision detection
    - allows for superscalar construction
- **Geometry Processing Stage**
    - Stages
        - Vertex Shading
        - Projection
        - Clipping
        - Screen Mapping
    - Vertex Shading
        1) compute pos for vertex
            - model transform -> world coordinates
        2) vertex output data (normal and texture coordinates)
        3) Optional
            - tessellation, geometry shading, stream output
    - Projection 
        - **Othographic** (type of parallel projection)
            - translation and scaling
            - parallel lines remain parallel
        - **Perspective** 
            - farther way obj is, smaller it appears
            - parallel lines may converge at horizon
        - Other Parallel Projections
            - Oblique, Axonometric projections
    - Clipping
        - 4-d homogenous coordinate to interpolate and clip when perspective project used
        - perspective division, triangle pos -> 3d normalized device coordinates
    - Screen Mapping
        - 3d -> screen coordinates
- **Rasterization** / Scan Conversion
    - find all pixels inside primitives being rendered
    1) Triangle setup / Primitive Assembly
        - equations, data computed
    2) Triangle traversal
        - Pixel's center/sample covered by triangle is checked
    - Send pixels/samples inside primitive sent to next stage
- **Pixel Processing**
    1) Pixel Shading
        - Pixel shader / fragment shader
    2) Merging
        - stencil buffer, framebuffer

## GPU


## Math
- x member of [X, Y) == X <= x < Y
- Special symbols
    - <img src="https://latex.codecogs.com/gif.latex?x^{&plus;}&space;=&space;\begin{cases}&space;1,&&space;\text{&space;if&space;}&space;x&space;>&space;0,&space;\\&space;0,&&space;otherwise.&space;\end{cases}" title="x^{+} = \begin{cases} 1,& \text{ if } x > 0, \\ 0,& otherwise. \end{cases}" />
    -<img src="https://latex.codecogs.com/gif.latex?x^{\overline{&plus;}}&space;=&space;\begin{cases}&space;1,&&space;\text{&space;if&space;}&space;x&space;\geq&space;1,&space;\\&space;x,&&space;\text{&space;if&space;}&space;0&space;<&space;x&space;<&space;1,&space;\\&space;0,&&space;otherwise.&space;\end{cases}" title="x^{\overline{+}} = \begin{cases} 1,& \text{ if } x \geq 1, \\ x,& \text{ if } 0 < x < 1, \\ 0,& otherwise. \end{cases}" />

- Binomials
    - <img src="https://latex.codecogs.com/gif.latex?\binom{n}{k}&space;=&space;\frac{n!}{k!\left&space;(&space;n&space;-&space;k&space;\right&space;)!}" title="\binom{n}{k} = \frac{n!}{k!\left ( n - k \right )!}" />
- Trig
    - <img src="https://latex.codecogs.com/gif.latex?\cos\Theta&space;=&space;\frac{a}{c}" title="\cos\Theta = \frac{a}{c}" />
    - <img src="https://latex.codecogs.com/gif.latex?\sin\Theta&space;=&space;\frac{b}{c}" title="\sin\Theta = \frac{b}{c}" />
    - <img src="https://latex.codecogs.com/gif.latex?\tan\Theta&space;=&space;\frac{\sin\Theta}{\cos\Theta}&space;=&space;\frac{a}{b}" title="\tan\Theta = \frac{\sin\Theta}{\cos\Theta} = \frac{a}{b}" />
    - pyhtagorean: <img src="https://latex.codecogs.com/gif.latex?a^2&plus;b^2=c^2" title="a^2+b^2=c^2" />
    - trig unity: <img src="https://latex.codecogs.com/gif.latex?\sin^2\Theta&space;&plus;&space;\cos^2\Theta&space;=&space;1" title="\sin^2\Theta + \cos^2\Theta = 1" />
    - law of cosines = <img src="https://latex.codecogs.com/gif.latex?c^2&space;=&space;a^2&space;&plus;&space;b^2&space;-&space;2ab\cdot&space;\cos\Theta" title="c^2 = a^2 + b^2 - 2ab\cdot \cos\Theta" />
    - law of sines = <img src="https://latex.codecogs.com/gif.latex?\frac{a}{\sin&space;A}&space;=&space;\frac{b}{\sin&space;B}&space;=&space;\frac{c}{\sin&space;C}" title="\frac{a}{\sin A} = \frac{b}{\sin B} = \frac{c}{\sin C}" />
- Point
    - Position, coordinates, vector representable
- Vector 
    - <img src="https://latex.codecogs.com/gif.latex?v&space;=&space;\begin{pmatrix}&space;v_{x}&space;\\&space;v_{y}&space;\\&space;v_{z}&space;\end{pmatrix}&space;=&space;(v_{x},&space;v_{y},&space;v_{z})^{T}" title="v = \begin{pmatrix} v_{x} \\ v_{y} \\ v_{z} \end{pmatrix} = (v_{x}, v_{y}, v_{z})^{T}" />
    - Direction and length, no position
    - magnitude/length of vector 
        - <img src="https://latex.codecogs.com/gif.latex?\left&space;\|&space;v&space;\right&space;\|" title="\left \| v \right \|" /></a>
    - transpose
        - <img src="https://latex.codecogs.com/gif.latex?v^{T}" title="v^{T}" />
        - turn col vec to row vec, row vec to col vec
    - Vector addition 
        - <img src="https://latex.codecogs.com/gif.latex?\vec{v}&space;&plus;&space;\vec{u}" title="\vec{v} + \vec{u}" />
        - translation: point + vector 
    - Vector subtraction
        - <img src="https://latex.codecogs.com/gif.latex?\vec{v}&space;-&space;\vec{u}" title="\vec{v} - \vec{u}" />
    - Scalar Vector Multiplication, <img src="https://latex.codecogs.com/gif.latex?k\vec{v}" title="k\vec{v}" />
        - if  k < 0, direction is opposite
    - normalized unit vector, has length 1, keeps angle
        - <img src="https://latex.codecogs.com/gif.latex?\check{u}&space;=&space;\frac{\vec{v}}{\left&space;\|\vec{v}&space;\right&space;\|}" title="\check{u} = \frac{\vec{v}}{\left \|\vec{v} \right \|}" />
    - Angle notation, <img src="https://latex.codecogs.com/gif.latex?\[u,&space;v\]" title="\[u, v\]" />
        - could also be perpendicular symbol
    - dot product
        - <img src="https://latex.codecogs.com/gif.latex?u&space;\cdot&space;v&space;=&space;\left&space;\|&space;u&space;\right&space;\|\left&space;\|&space;v&space;\right&space;\|\cos(\[u,&space;v\])" title="u \cdot v = \left \| u \right \|\left \| v \right \|\cos(\[u, v\])" />
            - positive if, 0 < angle < pi/2
            - negative, pi/2 < angle <= pi
            - 0, if perpendicular, angle = pi/2, u or v = 0
        - u dot v = u1v1 + u2v2 + u3v3..
        - **normal both vectors**, angle tends to be larger when closer together
        - dot on same itself = length <sup>2</sup>
    - law of cosine
        - <img src="https://latex.codecogs.com/gif.latex?w&space;(hypotenuse)&space;=&space;u&space;-&space;v" title="w (hypotenuse) = u - v" />
        - <img src="https://latex.codecogs.com/gif.latex?\left&space;\|&space;w&space;\right&space;\|^{2}&space;=&space;\left&space;\|&space;u&space;\right&space;\|^{2}&space;&plus;&space;\left&space;\|&space;v&space;\right&space;\|^{2}&space;-&space;2\left&space;\|&space;u&space;\right&space;\|&space;\left&space;\|&space;v&space;\right&space;\|&space;\cos\[u,&space;v\]" title="\left \| w \right \|^{2} = \left \| u \right \|^{2} + \left \| v \right \|^{2} - 2\left \| u \right \| \left \| v \right \| \cos\[u, v\]" />
    - angle of two non-zero vectors
        - <img src="https://latex.codecogs.com/gif.latex?[u,&space;v]&space;=&space;\frac{\arccos&space;\left&space;(u&space;\cdot&space;v&space;\right&space;)}{\left&space;\|&space;u&space;\right&space;\|\left&space;\|&space;v&space;\right&space;\|}" title="[u, v] = \frac{\arccos \left (u \cdot v \right )}{\left \| u \right \|\left \| v \right \|}" />
    - Orthogonal / Pependicular 
        - <img src="https://latex.codecogs.com/gif.latex?v\perp&space;u" title="v\perp u" />
        - if angle makes 90 deg,  dot product = 0
        - orthogonal projection
            - <img src="https://latex.codecogs.com/gif.latex?Proj_{v}&space;u&space;=&space;\left&space;(&space;\frac{u&space;\cdot&space;v&space;}{\left&space;\|&space;v&space;\right&space;\|^{2}}&space;\right&space;)&space;v" title="Proj_{v} u = \left ( \frac{u \cdot v }{\left \| v \right \|^{2}} \right ) v" />
            - produce 3d image to 2d image, useful for 3d -> 2d
    - Parallel:  
        - <img src="https://latex.codecogs.com/gif.latex?v\parallel&space;u" title="v\parallel u" />
        - on same line, direction could be opposite
        - Collinear 
            - vectors have same direction
    - cauchy-schwarz inequality
        - <img src="https://latex.codecogs.com/gif.latex?(u&space;\cdot&space;v&space;)^{2}&space;\leq&space;\left&space;\|&space;u&space;\right&space;\|^{2}&space;\left&space;\|&space;v&space;\right&space;\|^{2}" title="(u \cdot v )^{2} \leq \left \| u \right \|^{2} \left \| v \right \|^{2}" />
    - triangle inequality, <img src="https://latex.codecogs.com/gif.latex?\left&space;\|&space;u&space;&plus;&space;v&space;\right&space;\|&space;\leq&space;\left&space;\|&space;u&space;\right&space;\|&space;&plus;&space;\left&space;\|&space;v&space;\right&space;\|" title="\left \| u + v \right \| \leq \left \| u \right \| + \left \| v \right \|" />
    - Cross product
        - find vector perpendicular to plane
        - calculate how light reflects off surface
    - Coordinates
        - 3D: v = xe1 + ye2 + ze3 = (vx vy vz)
    - Canonical Basis (identity matrix basically)
        - <img src="https://latex.codecogs.com/gif.latex?v&space;=&space;\begin{pmatrix}&space;1&space;&&space;0&space;&&space;0&space;\\&space;0&space;&&space;1&space;&&space;0&space;\\&space;0&space;&&space;0&space;&&space;1&space;\end{pmatrix}" title="v = \begin{pmatrix} 1 & 0 & 0 \\ 0 & 1 & 0 \\ 0 & 0 & 1 \end{pmatrix}" />
    - 
- Displacement
    - finalPoint - startingPoint then use vector to move
- Line
    - starting point S, direction d
    - parameterized line
        - P(t) = S + td
            - t is scalar
- Plane
    - similar to lines, 3d or higher, splits domain into two half-spaces
    - two non-co-linear vectors define a plane
    - parameterized plane
        - P(t1, t2) S + t1d1 + t2d2
    - implicit plane eq
        - n dot (P - S) = 0
    - orthogonal projection onto plane
        - Q = P - Pn v
            - v = P - S
    - Shadow Projection on a Plane
    - Ray-sphere intersection
        - ||P - C|| = r 
            - P points on surface, C center point, r as radius
        - R(t) = S + td
            - t = (-b +- sqrt(b<sup>2</sup> - ac)) / a
    - Law of reflection
        - r = i - Pn i - Pn i
            - r = i - 2 ((i dot n) / ||n||<sup>2</sup>)n

    - http://immersivemath.com/ila/ch04_vectorproduct/ch04.html
# Resources
- Real-Time Rendering Fourth Edition 
    - http://www.realtimerendering.com/#intro
- http://immersivemath.com/ila/index.html
    - Linear Algebra
- http://programmedlessons.org/VectorLessons/index.html
    - Very good practice on vectors and matrices\
- https://www.codecogs.com/latex/eqneditor.php
    - Useful for creating HTML image tags for equations