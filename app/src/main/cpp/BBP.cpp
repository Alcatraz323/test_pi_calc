#include "BBP.h"
#include <math.h>

double BBP::calcBBP(int precision) {
    double result = 0;
    for (int iterator = 0; iterator <= precision; iterator++) {
        double precisionOffset = 1.0 / pow(16, iterator);
        double s1 = 4.0 / (8 * iterator + 1);   // 计算4/(8k+1)
        double s2 = 2.0 / (8 * iterator + 4);   // 计算2/(8k+4)
        double s3 = 1.0 / (8 * iterator + 5);   // 计算1/(8k+5)
        double s4 = 1.0 / (8 * iterator + 6);   // 计算1/(8k+6)
        double currentResult = precisionOffset * (s1 - s2 - s3 - s4); //计算当前循环结果
        result += currentResult;    //添加当前位数结果
    }
    result = ((int) (result * pow(10, precision))) / pow(10, precision);    //舍去不准确位数
    return result;
}
