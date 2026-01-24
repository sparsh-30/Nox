import React from "react";
import { FadeLoader } from "react-spinners";

const LoadingScreen = () => {
  return (
    <div className="w-screen h-screen flex z-100 items-center justify-center absolute top-0 left-0 bg-white/50">
      <FadeLoader
        height={32}
        margin={10}
        radius={10}
        width={5}
        color="#007F78"
      />
    </div>
  );
};

export default LoadingScreen;
