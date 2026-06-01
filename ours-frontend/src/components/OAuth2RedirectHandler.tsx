import { useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";

const OAuth2RedirectHandler = () => {
    const navigate = useNavigate();
    const isProcessed = useRef(false);

    useEffect(() => {
        if (isProcessed.current) return;
        isProcessed.current = true;

        const params = new URLSearchParams(window.location.search);
        const accessToken = params.get("accessToken");
        const refreshToken = params.get("refreshToken");

        if (accessToken && refreshToken) {
            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("refreshToken", refreshToken);
            alert("로그인 성공!");
            navigate("/");
        } else {
            alert("로그인 실패");
            navigate("/");
        }
    }, [navigate]);

    return <div>로그인 처리 중...</div>;
};

export default OAuth2RedirectHandler;