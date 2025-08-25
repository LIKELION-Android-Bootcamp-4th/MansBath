/**
 * 에러가 발생하는 부분을 자세하게 확인하기 위해 유틸 함수로 분리했습니다.
 *
 * @param {any} error - 처리할 예외 객체
 * @return {FormattedError} 에러 정보를 정리한 객체
 */
export function formatError(error: any) {
  return {
    message: error.message || "Unknown error",
    name: error.name || "Unknown Error",
    stack: error.stack || null,
    code: error.code || null,
    cause: error.cause || null,
    raw: JSON.stringify(error, Object.getOwnPropertyNames(error)),
  };
}
