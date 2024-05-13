import router from '..'

function useCachedGuard() {
  router.beforeEach(() => {
    return true
  })
}

export default useCachedGuard
