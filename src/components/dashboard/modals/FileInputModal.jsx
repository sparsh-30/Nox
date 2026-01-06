import { useState, useRef } from "react";
import { MdCancelPresentation } from "react-icons/md";
import { FaRegCheckSquare } from "react-icons/fa";
import useObjectStore from "../../../utils/store/ObjectStore";
import useSelectedContainerStore from "../../../utils/store/SelectedContainerStore";
import useParentPathStore from "../../../utils/store/ParentPathStore";
import useModalToggleStore from "../../../utils/store/ModalToggleStore";
import { axiosInstance } from "../../../utils/AxiosConfig";
import { formatBytes } from "../../../utils/CommonMethods";

const FileInputModal = () => {
  const CHUNK_SIZE = 4 * 1024 * 1024; // 4MB
  const MAX_UPLOAD_SIZE = 100 * 1024 * 1024; // 100 MB

  const fileRef = useRef(null);
  const abortRef = useRef(null);

  const [progress, setProgress] = useState(0);
  const [uploading, setUploading] = useState(false);
  const [isSubmitDisabled, setIsSubmitDisabled] = useState(true);
  const [showSizeWarning, setShowSizeWarning] = useState(false);

  const objects = useObjectStore((state) => state.objects);
  const addNewObject = useObjectStore((state) => state.addNewObject);
  const selectedContainerId = useSelectedContainerStore(
    (state) => state.selectedContainerId
  );
  const currentParentPath = useParentPathStore(
    (state) => state.currentParentPath
  );

  const closeFileInputModal = useModalToggleStore(
    (state) => state.closeFileInputModal
  );

  const onFileChange = (e) => {
    const files = e.target.files;

    if (!files || files.length === 0) {
      fileRef.current = null;
      setShowSizeWarning(false);
      setIsSubmitDisabled(true);
      return;
    }

    fileRef.current = files[0];
    if (files[0].size > MAX_UPLOAD_SIZE) {
      setIsSubmitDisabled(true);
      setShowSizeWarning(true);
      return;
    }
    setShowSizeWarning(false);
    setIsSubmitDisabled(false);
  };

  const handleFileUpload = async () => {
    const file = fileRef.current;
    if (!file || isSubmitDisabled) return;

    try {
      const sessionIdResponse = await axiosInstance.get("/api/v1/upload/start");

      const sessionId = sessionIdResponse.data.payload.sessionId;
      const totalChunks = Math.ceil(file.size / CHUNK_SIZE);

      setUploading(true);
      abortRef.current = new AbortController();

      for (let i = 0; i < totalChunks; i++) {
        const start = i * CHUNK_SIZE;
        const end = Math.min(start + CHUNK_SIZE, file.size);
        const chunk = file.slice(start, end);

        const formData = new FormData();
        formData.append("chunk", chunk);
        formData.append("sessionId", sessionId);
        formData.append("chunkNumber", i);

        await axiosInstance.post("/api/v1/upload/chunk", formData, {
          signal: abortRef.current.signal,
        });

        setProgress(Math.round(((i + 1) / totalChunks) * 100));
      }

      const mergeChunksResponse = await axiosInstance.put(
        "/api/v1/upload/merge",
        null,
        {
          params: {
            sessionId: sessionId,
            totalChunks: totalChunks,
            objectName: file.name,
            size: file.size,
            parentPath: currentParentPath,
            parentContainerId: selectedContainerId,
          },
        }
      );
      addNewObject(mergeChunksResponse.data.payload);
      closeFileInputModal();
    } catch (e) {
      console.log(e);
    }
  };

  return (
    <div className="h-screen w-screen z-50 absolute top-0 left-0 bg-black/30 backdrop-blur-sm flex items-center justify-center">
      <div className="bg-secondary-bg h-fit bp:h-fit w-9/10 bp:w-4/10 px-10 py-10 gap-10 rounded-xl">
        <div>Please select the file to upload:</div>
        <div className="mt-5 border border-primary-border rounded-xl">
          <input
            type="file"
            className="block w-full text-sm text-gray-500 file:m-2 file:py-2 file:px-4 file:rounded-lg file:border-0 file:text-sm file:font-semibold file:bg-brand-primary-600 file:text-secondary-text hover:file:bg-brand-primary-400 file:disabled:opacity-50 file:disabled:pointer-events-none"
            onChange={(e) => onFileChange(e)}
          />
        </div>
        {/* <div className="text-xs text-warning">
          * The file name that you selected already exists in the folder.
          Re-uploading will overwrite the existing file.
        </div> */}
        {progress}
        {showSizeWarning ? (
          <div className="text-xs text-danger">
            * Max file size allowed: {formatBytes(MAX_UPLOAD_SIZE)}
          </div>
        ) : (
          ""
        )}
        <div className=" mt-5 flex justify-center items-center gap-4">
          <div
            onClick={() => closeFileInputModal()}
            className="w-1/2 py-2 px-3 font-semibold bg-danger text-secondary-text rounded-md flex items-center justify-center gap-1 cursor-pointer transition-all duration-100 ease-in-out hover:scale-105"
          >
            <div>Close</div>
            <div className="text-lg">
              <MdCancelPresentation />
            </div>
          </div>
          <div
            onClick={handleFileUpload}
            className={`w-1/2 py-2 px-3 font-semibold text-secondary-text rounded-md flex items-center justify-center gap-1 transition-all duration-100 ease-in-out ${
              isSubmitDisabled
                ? "bg-brand-primary-600/50 cursor-not-allowed"
                : "bg-brand-primary-600 cursor-pointer hover:scale-105"
            }`}
          >
            <div>Confirm</div>
            <div className="text-lg">
              <FaRegCheckSquare />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default FileInputModal;
