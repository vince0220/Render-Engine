package Engine.Algebra.Matrices;

/*
    Partially found on the internet. Contains almost all functionality that a matrix class should have.
    Used because of lack of knowledge about the precise workings of the algorithms behind matrices. Complex calculations like the inverse or determinant
 */
public class Matrix {
    public float[][] data;
    public int rows;
    public int columns;

    public Matrix(int rows, int columns){
        this.rows = rows;
        this.columns = columns;
        data = new float[rows][columns];
    }
    public Matrix(float[][] values){
        this.rows = values.length;
        this.columns = values[0].length;
        data = values;
    }
    public Matrix(Matrix A){
        this(A.rows,A.columns);
        for(int i=0; i<rows; ++i){
            for(int j=0; j<columns; ++j){
                this.data[i][j] = A.data[i][j];
            }
        }
    }
    public Matrix ScalarMult(float a){
        Matrix A = this;
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                A.data[i][j] *= a;
            }
        }
        return A;
    }
    public float Norm(){
        Matrix A = this;
        if(A.columns != 1)
            throw new RuntimeException("Illegal vector dimensions");
        float norm = 0.0f;
        for(int i = 0; i<A.rows; i++){
            norm += A.data[i][0]*A.data[i][0];
        }
        norm = (float)Math.pow(norm, 0.5);
        return norm;
    }
    public Matrix Plus(float a){
        Matrix A = this;
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                A.data[i][j] += a;
            }
        }
        return A;
    }
    public Matrix Minus(float a){
        Matrix A = this;
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                A.data[i][j] -= a;
            }
        }
        return A;
    }
    public void SwapRows(int i, int j){
        Matrix A = this;
        float[] temp = A.data[i];
        A.data[i] = A.data[j];
        A.data[j] = temp;
    }
    public Matrix Transpose() {
        Matrix A = new Matrix(columns, rows);
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                A.data[j][i] = this.data[i][j];
            }
        }
        return A;
    }
    public Matrix Plus(Matrix B){
        Matrix A = this;
        if(A.rows != B.rows || A.columns != B.columns){
            throw new RuntimeException("Unequal matrix dimensions");
        }
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                A.data[i][j] += B.data[i][j];
            }
        }
        return A;
    }
    public Matrix Minus(Matrix B){
        Matrix A = this;
        if(A.rows != B.rows || A.columns != B.columns){
            throw new RuntimeException("Unequal matrix dimensions");
        }
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                A.data[i][j] -= B.data[i][j];
            }
        }
        return A;
    }
    public Matrix Multiply(Matrix B){
        Matrix A = this;
        if (A.columns != B.rows) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(A.rows, B.columns);
        for (int i = 0; i < C.rows; i++)
            for (int j = 0; j < C.columns; j++)
                for (int k = 0; k < A.columns; k++)
                    C.data[i][j] += (A.data[i][k] * B.data[k][j]);
        return C;
    }
    public void PrintAnswers(){
        Matrix A = this;
        boolean solutionExists = true;
        boolean done = false;
        int i = 0;
        while(!done && i<A.rows){
            boolean allZeros = true;
            for(int j=0; j<A.columns-1; ++j){
                if(A.data[i][j] != 0){
                    allZeros = false;
                }
            }
            //If the row of R is all zeros but d isn't
            if(allZeros && A.data[i][A.columns-1] != 0){
                solutionExists = false;
                System.out.println("No solution.");
            }
            else if(allZeros && A.data[i][A.columns-1] == 0){
                done = true;
                System.out.println("Infinite Solutions");
            }
            else if(A.rows<(A.columns-1)){
                done = true;
                System.out.println("Infinite solutions");
            }
            i++;
        }
        if(!done){
            System.out.println("Unique Solution");
        }
        if(solutionExists){
            Matrix particular = new Matrix(A.columns-1, 1);
            boolean[] freeMarker = new boolean[A.columns-1];
            int n = 0;
            for(i=0; i<A.rows; ++i){
                boolean pivotFound = false;
                //int free2 = 0;
                for(int j=0; j<A.columns-1; ++j){
                    if(A.data[i][j] != 0){
                        //If the pivot is found, add to particular
                        if(!pivotFound){
                            pivotFound = true;
                            particular.data[j][0] = A.data[i][A.columns-1];
                            freeMarker[j] = false;
                        }
                        //Only want to create one set of special solutions
                        else{
                            freeMarker[j] = true;
                        }

                    }

                }
            }
            System.out.println("Particular Solution");

            particular.PrintMatrix();
            for(int j = 0; j<A.columns-1; j++){
                //Create a special solution for each free variable
                if(freeMarker[j]){
                    Matrix special = new Matrix(A.columns-1, 1);
                    special.data[j][0] = 1;
                    for(int k = 0; k<A.columns-1; k++){
                        //Set the non-free variables of the special solutions to the negated values of column
                        if(!freeMarker[k]){
                            for(int z = 0; z<A.rows; z++){
                                if(A.data[k][z] == 1){
                                    special.data[k][0] = -1*A.data[z][j];
                                }
                            }
                        }
                    }
                    System.out.println("Special Solution");
                    special.PrintMatrix();
                }

            }

        }
    }
    public void PrintMatrix(){
        Matrix A = this;
        for(int i = 0; i<A.rows; i++){
            for(int j = 0; j<A.columns; j++){
                System.out.print(A.data[i][j]+" ");
            }
            System.out.println();
        }
    }
    public float determinant() {
        if (rows != columns) {
            return Float.NaN;
        }
        else {
            return _determinant(this);
        }
    }
    public Matrix Inverse() {
        double det = determinant();

        if (rows != columns || det == 0.0) {
            return null;
        }
        else {
            Matrix result = new Matrix(rows, columns);

            for (int row = 0; row < rows; ++row) {
                for (int col = 0; col < columns; ++col) {
                    Matrix sub = subMatrix(this, row + 1, col + 1);

                    result.data[col][row] = (float)(1.0 / det *
                            Math.pow(-1, row + col) *
                            _determinant(sub));
                }
            }

            return result;
        }
    }
    private float _determinant(Matrix matrix) {
        if (matrix.columns == 1) {
            return matrix.data[0][0];
        }
        else if (matrix.columns == 2) {
            return (matrix.data[0][0] * matrix.data[1][1] -
                    matrix.data[0][1] * matrix.data[1][0]);
        }
        else {
            double result = 0.0;

            for (int col = 0; col < matrix.columns; ++col) {
                Matrix sub = subMatrix(matrix, 1, col + 1);

                result += (Math.pow(-1, 1 + col + 1) *
                        matrix.data[0][col] * _determinant(sub));
            }

            return (float)result;
        }
    }
    public Matrix subMatrix(Matrix matrix, int exclude_row, int exclude_col) {
        Matrix result = new Matrix(matrix.rows - 1, matrix.columns - 1);

        for (int row = 0, p = 0; row < matrix.rows; ++row) {
            if (row != exclude_row - 1) {
                for (int col = 0, q = 0; col < matrix.columns; ++col) {
                    if (col != exclude_col - 1) {
                        result.data[p][q] = matrix.data[row][col];

                        ++q;
                    }
                }

                ++p;
            }
        }

        return result;
    }
}