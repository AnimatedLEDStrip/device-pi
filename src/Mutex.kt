import kotlinx.coroutines.sync.Mutex

inline fun <T> Mutex.tryWithLock(owner: Any? = null, action: () -> T) {
    if (tryLock(owner)) {
        try {
            action()
            return
        } finally {
            unlock(owner)
        }
    } else {
        println("Access Overlap")
        return
    }
}