#!/bin/sh

images=$( aws ecr list-images --repository-name ${AWS_ECR_REPOSITORY} --query 'imageIds[?type(imageTag)!=`string`].[imageDigest]' --output text )

for digest in ${images}
    do
        aws ecr batch-delete-image --repository-name ${AWS_ECR_REPOSITORY} --image-ids imageDigest=${digest}
    done