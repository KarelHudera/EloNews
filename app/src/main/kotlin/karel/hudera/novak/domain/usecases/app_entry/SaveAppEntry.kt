package karel.hudera.novak.domain.usecases.app_entry

import karel.hudera.novak.domain.manger.LocalUserManager

class SaveAppEntry(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(){
        localUserManager.saveAppEntry()
    }
}