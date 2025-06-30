
import { create } from 'zustand';

interface OrcProps {
    orcResult: string,
    setOrcResult: (result: string) => void
}
interface UserInfomation {
    userName: string,
    userEmail: string,
}
interface UserProps {
    user: UserInfomation,
    setUser: (user: UserInfomation) => void
    accessToken: string,
    setAccessToken: (accessToken: string) => void
}



export const UserState = create<UserProps>((set) => ({
    user: {
        userName: "",
        userEmail: ""
    },
    setUser: (user: UserInfomation) => set({ user }),
    accessToken: "",
    setAccessToken: (accessToken: string) => set({ accessToken })
}))

export const OrcState = create<OrcProps>((set) => ({
    orcResult: "",
    setOrcResult: (orcResult: string) => set({ orcResult })
}))

