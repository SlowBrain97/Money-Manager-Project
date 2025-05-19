
import {create} from 'zustand';

interface OrcProps {
    orcResult : string,
    setOrcResult : (result: string) => void
}

export const OrcState = create<OrcProps>((set) => ({
    orcResult : "",
    setOrcResult: (orcResult:string) => set({orcResult})
}))

