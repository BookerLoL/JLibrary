public class Matrix {
  double[][] matrix;
  //row major: [row][col]  [0][0], [0][1], [0][2]
  //col major: [col][row]  [0][0], [1][0], [2][0]
  
  public Matrix(int width, int height) {
    matrix = new double[height][width];
  }

  // matrix[i][j]
  public double get(int row, int col) {
    return matrix[row][col];
  }

  public void set(int row, int col, double value) {
    matrix[row][col] = value;
  }

  public boolean isSymmetric() {
   if (!isSquare()) {
     return false;
   }

    for(int i = 1; i < matrix.length; i++) {
      for(int j = 0; j < i; j++) {
        if (matrix[i][j] != matrix[j][i]) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean isAntiSymmetric() {
    if ( !isSquare() ) {
     return false;
   }

    for (int i = 0; i < matrix.length; i++) {
      if ( matrix[i][i] != 0 ) {
        return false;
      }
    }

     for(int i = 1; i < matrix.length; i++) {
      for(int j = 0; j < i; j++) {
        if ( matrix[i][j] != (-matrix[j][i]) ) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean isSquare() {
    return rowLength() == colLength();
  }

  public int rowLength() {
    return matrix.length;
  }

  public int colLength() {
    return matrix[0].length;
  }

  public boolean isVerticalVector() {
    return colLength() == 1;
  }

  public boolean isRowVector() {
    return rowLength() == 1;
  }

  public boolean isSameSize(Matrix m) {
    return m.rowLength() == this.rowLength() && m.colLength() == this.colLength();
  }

  public Matrix addMatrix(Matrix m) {
    if ( !isSameSize(m) ) {
      return null;
    }

    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; i++) {
        matrix[i][j] += m.get(i, j);
      }
    }
    return this;
  }

  public Matrix minusMatrix(Matrix m) {
    if ( !isSameSize(m) ) {
      return null;
    }

    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; i++) {
        matrix[i][j] -= m.get(i, j);
      }
    }
    return this;
  }

  public Matrix multiply(double scalar) {
    for(int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        matrix[i][j] *= scalar;
      }
    }
    return this;
  }

  
  public Matrix product(Matrix m) {
    if (! (colLength() == m.rowLength())) {
      return null;
    }

    Matrix productMatrix = new Matrix(rowLength(), m.colLength());

    for (int i = 0; i < rowLength(); i++) {
      for (int j = 0; j < m.colLength(); j++) {
        productMatrix.set(i, j, productHelper(i, this, j, m));
      }
    }
    return productMatrix;
  }

  private double productHelper(int row, Matrix first, int col, Matrix second) {
    double sum = 0.0;
    for (int i = 0; i < first.colLength(); i++) {
      sum += first.get(row, i) * second.get(i, col);
    }
    return sum;
  }


  Matrix transpose(Matrix m) {
    Matrix t = new Matrix(m.colLength(), m.rowLength());
     for (int i = 0; i < m.rowLength(); i++) {
        for (int j = 0; j < m.colLength(); j++) {
          t.set(j, i, m.get(i, j));
        }
     }
     return t;
  }


  /*
  Dot product of vector: a.x * b.x + a.y * b.y ...
  a dot b == ||a|| ||b|| cos t

  right-hand rule -> x on right, y forward, z top
  */
}
