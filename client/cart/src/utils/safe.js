export default function safePromise(promise) {
  return promise
  .then(result => [undefined, result.data])
  .catch(error => [error, undefined]);
}

