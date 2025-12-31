'use client'
import { useRouter } from "next/navigation";
import Qnas from "./component/QnaList";


export default function qnaHome() {

    const router = useRouter();

    return(
        <div>
            <h1>나의 문의 사항</h1>
            <Qnas/>
            <button type="button" onClick={()=> router.push("/CreateQna")}>
                문의하기
            </button>
        </div>
    )

}