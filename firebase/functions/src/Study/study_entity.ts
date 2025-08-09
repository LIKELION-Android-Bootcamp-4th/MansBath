export interface Study{
    title: string,
    description: string,
    duration: string,
    status: boolean,
    items: StudyDetail[]
}
export interface StudyDetail{
    title : string,
    subtitle : string[],
    content: ContentDetail,
}
export interface ContentDetail{
    overview: string,
    keyPoints: string[],
    details: string,
}
